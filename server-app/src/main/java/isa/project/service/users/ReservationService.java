package isa.project.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.reservations.ReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.aircompany.TicketReservation;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.Reservation;
import isa.project.repository.users.ReservationRepository;
import isa.project.service.aircompany.FlightReservationsService;
import isa.project.service.hotel.RoomReservationService;
import isa.project.service.rentacar.CarReservationService;

@Service
public class ReservationService {
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
	
	/**
	 * Kreira rezervaciju na osnovu korpe.
	 * @param reservationRequest - korpa sa podacima za neke od pojedinacne tri rezervacije
	 * @param email - email korisnika koji vrsi rezervaciju
	 * @return - kreira (i sacuvana) rezervacija
	 * @throws ResourceNotFoundException - nije pronadjen let ili karta ili prijateljstvo
	 * @throws ReservationNotAvailable - zauzeto neko od mesta u avionu
	 * @throws RequestDataException - ne postoji barem jedna rezervacija, nije poslana oznaka prijatelja, a oznaceno je da se poziva prijetelj
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
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
		
		System.out.println("aaa");
		Reservation saved = reservationRepository.save(reservation);
		flightReservationsService.saveActiveTickets(saved);
		flightReservationsService.saveFriendInvites(saved, reservationRequest, customer.getId());
		
		return saved;
	}
	
	/**
	 * Salje pozivnice prijateljima, ukoliko ih rezervacija sadrzi.
	 * @param reservation
	 */
	public void sendFriendInvites(Reservation reservation) {
		if(reservation.getFlightReservation() != null) {
			for(TicketReservation ticketReservation: reservation.getFlightReservation().getTicketReservations()) {				
				if(ticketReservation.getInvitedFriend() != null) {
					sendFriendInviteEmail();
				}
			}
		}
	}
	
	private void sendFriendInviteEmail() {
		// TODO:
		System.out.println("Sending friend invite.");
	}
}
