package isa.project.service.users;

import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.reservations.ReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.aircompany.TicketReservation;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.Reservation;
import isa.project.repository.users.ReservationRepository;
import isa.project.service.EmailService;
import isa.project.service.aircompany.FlightReservationsService;
import isa.project.service.hotel.RoomReservationService;
import isa.project.service.rentacar.CarReservationService;

@Service
public class ReservationService {
	private static String ourPageUrl = "https://www.google.com/";
	
	@Autowired
	private FlightReservationsService flightReservationsService;
	
	@Autowired
	private RoomReservationService roomReservationService;
	
	@Autowired
	private CarReservationService carReservationService;
	
	@Autowired
	private CustomerService customerService;	
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private EmailService emailService;
	
	/**
	 * Kreira rezervaciju na osnovu korpe.
	 * @param reservationRequest - korpa sa podacima za neke od pojedinacne tri rezervacije
	 * @param email - email korisnika koji vrsi rezervaciju
	 * @return - kreira (i sacuvana) rezervacija
	 * @throws ResourceNotFoundException - nije pronadjen let ili karta ili prijateljstvo
	 * @throws ReservationNotAvailable - zauzeto neko od mesta u avionu
	 * @throws RequestDataException - ne postoji barem jedna rezervacija, nije poslana oznaka prijatelja, a oznaceno je da se poziva prijetelj
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Reservation reserve(ReservationRequestDTO reservationRequest, String email) throws ResourceNotFoundException, ReservationNotAvailable, RequestDataException {
		// preuzmi kupca
		Customer customer = customerService.findCustomerByEmail(email);
						
		//kreiraj novu rezervaciju
		Reservation reservation = new Reservation();
		reservation.setCustomer(customer);
		
		boolean atLeastOneReservation = false;
		
		//dodaj odgovarajuce delove rezervacije
		if(reservationRequest.getFlightReservationRequest() != null) {
			FlightReservation flightReservation = flightReservationsService.reserve(reservationRequest.getFlightReservationRequest(), customer);
			reservation.setFlightReservation(flightReservation);
			atLeastOneReservation = true;
		}
		
		if(reservationRequest.getCarReservationRequest() != null) {
			CarReservation carReservation = carReservationService.reseve(reservationRequest.getCarReservationRequest());
			reservation.setCarReservation(carReservation);
			atLeastOneReservation = true;
		}
		
		if(reservationRequest.getHotelReservationRequest() != null) {
			RoomReservation roomReservation = roomReservationService.reserve(reservationRequest.getHotelReservationRequest());
			System.out.println("Cuvamo room rezervaciju");
			reservation.setRoomReservation(roomReservation);
			System.out.println("Sacuvana");
			atLeastOneReservation = true;
		}
		
		if(!atLeastOneReservation) {
			throw new RequestDataException("There must be at least one sub reservation.");
		}
		
		//postavi 5% popusta na rezervacije koje imaju sva tri dela
		if(reservation.getFlightReservation()!=null && reservation.getRoomReservation()!=null && reservation.getCarReservation()!=null) {
			reservation.setDiscount(reservation.getDiscount() + 0.05);
		}
		
		//dodaj 10% popusta ako je onaj ko porucuje presao preko 10000 pre ova rezervacije
		if(customer.getLengthTravelled() >= 10000.0) {
			reservation.setDiscount(reservation.getDiscount() + 0.1);
		}
		
		//sacuvaj nove predjene kilometre kupcu
		if(reservation.getFlightReservation() != null) {
			customer.setLengthTravelled(customer.getLengthTravelled() + reservation.getFlightReservation().getFlight().getLength());
			customerService.saveCustomer(customer);
		}
		
		System.out.println("aaa");
		Reservation saved = reservationRepository.save(reservation);
		flightReservationsService.saveActiveTickets(saved);
		flightReservationsService.saveFriendInvites(saved, reservationRequest, customer.getId());
		
		return saved;
	}
	
	/**
	 * Salje pozivnice prijateljima, ukoliko ih rezervacija sadrzi.
	 * @param reservation
	 * @throws MessagingException 
	 * @throws InterruptedException 
	 * @throws MailException 
	 */
	public void sendFriendInvites(Reservation reservation) throws MailException, InterruptedException, MessagingException {
		if(reservation.getFlightReservation() != null) {
			Customer whoInvited = reservation.getCustomer();
			for(TicketReservation ticketReservation: reservation.getFlightReservation().getTicketReservations()) {				
				if(ticketReservation.getInvitedFriend() != null) {
					sendFriendInviteEmail(ticketReservation.getInvitedFriend().getFriend(), whoInvited);
				}
			}
		}
	}
	
	/**
	 * Salje poruku jednom prijatelju.
	 * @param invited
	 * @param whoInvited
	 * @throws MailException
	 * @throws InterruptedException
	 * @throws MessagingException
	 */
	private void sendFriendInviteEmail(Customer invited, Customer whoInvited) throws MailException, InterruptedException, MessagingException {
		String message = whoInvited.getFirstName() + " " + whoInvited.getLastName() + " has just invited you to travel with them.";
		message += " For more information click <a href=\"" + ourPageUrl + "\"> here</a>.";
		emailService.sendNotificaitionAsync(invited.getEmail(), "Trip invite", message);
	}
	
	/**
	 * Salje mejl kupcu.
	 * @param reservation
	 * @throws MailException
	 * @throws InterruptedException
	 * @throws MessagingException
	 */
	public void sendMailToUser(Reservation reservation) throws MailException, InterruptedException, MessagingException {
		Customer customer = reservation.getCustomer();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
		SimpleDateFormat formatF = new SimpleDateFormat("dd.MM.YYYY hh:mm");
		String message = "<p>Resevation has been made using this account. Reservation contains following items: </p>";
		if(reservation.getFlightReservation() != null) {
			Flight flight = reservation.getFlightReservation().getFlight();
			message += "<h1>Flight reservation </h1><hr>";
			message += "<p>You have booked " + reservation.getFlightReservation().getTicketReservations().size() + " tickets for ";
			message += " flight from " + flight.getDestinations().get(0) + " to " + flight.getDestinations().get(flight.getDestinations().size()-1) + ".";
			message += " Flight take off is scheduled for " + formatF.format(flight.getStartDateAndTime());
			message += " and landing time is " + formatF.format(flight.getEndDateAndTime()) + ". </p>";
		}
		
		if(reservation.getRoomReservation() != null) {
			RoomReservation roomRes = reservation.getRoomReservation();
			Hotel hotel = roomRes.getSingleRoomReservations().stream().findAny().get().getRoom().getHotel();
			message += "<h1>Hotel reservation </h1><hr>";
			message += "<p> You booked " + roomRes.getSingleRoomReservations().size() + " rooms in hotel ";
			message += hotel.getName() + ", " + hotel.getLocation() + ".";
			message += " Check in date is " + format.format(roomRes.getCheckInDate()) + " and check out date is " + format.format(roomRes.getCheckOutDate()) + ". </p>";
		}
		
		if(reservation.getCarReservation() != null) {
			CarReservation carReservation = reservation.getCarReservation();
			Car car = carReservation.getCar();
			message += "<h1>Car reservation </h1><hr>";
			message += "You have rented car " + car.getModel() + ", " + car.getBrand() + " from " + car.getRentACarCompany().getName() + ".";
			message += " Car pickup is on " + format.format(carReservation.getPickUpDate()) + " at " + carReservation.getPickUpBranchOffice() + ". ";
			message += " Car drop off is on " + format.format(carReservation.getDropOffDate()) + " at " + carReservation.getDropOffBranchOffice() + ".";
		}
		
		message += "<p>For more details click <a href=\"" + ourPageUrl + "\"> here</a>.</p>";
		message += "<p>Thank you for using our site. </p>";
		emailService.sendNotificaitionAsync(customer.getEmail(), "Trip reservation", message);
	}
	
	/**
	 * Otkazivanje leta, koje otkazuje i rezervaciju hotela i rent-a-car-a.
	 * @param id - oznaka rezervacije.
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws RequestDataException
	 */
	public Reservation cancelReservation(Integer id, String email) throws ResourceNotFoundException, RequestDataException {
		Reservation reservation = findReservation(id);
		if(!reservation.getCustomer().getEmail().equals(email)) {
			throw new RequestDataException("You don't have reservation with this id.");
		}
		if(reservation.getFlightReservation() !=null ) {
			flightReservationsService.cancelReservation(reservation.getFlightReservation());
		}
		//TODO: Ovde dodati da se otkazu hotel i rent-a-car
		return reservation;
	}
	
	private Reservation findReservation(Integer id) throws ResourceNotFoundException {
		Optional<Reservation> reservationOpt = reservationRepository.findById(id);
		if(!reservationOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Reservation not found");
		}
		return reservationOpt.get();
	}
}
