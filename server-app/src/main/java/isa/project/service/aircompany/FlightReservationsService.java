package isa.project.service.aircompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.reservations.FlightReservationRequestDTO;
import isa.project.dto.reservations.ReservationRequestDTO;
import isa.project.dto.reservations.TicketReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.Flight.FlightStatus;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.Ticket.TicketStatus;
import isa.project.model.aircompany.TicketForFastReservation;
import isa.project.model.aircompany.TicketReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.FriendInvite;
import isa.project.model.users.Reservation;
import isa.project.model.users.friendship.Friendship;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.FlightRepository;
import isa.project.repository.aircompany.TicketForFastReservationRepository;
import isa.project.repository.aircompany.TicketRespository;
import isa.project.repository.users.FriendInvitesRepository;
import isa.project.repository.users.FriendshipRepository;
import isa.project.repository.users.ReservationRepository;
import isa.project.service.users.CustomerService;

@Service
public class FlightReservationsService {
	@Autowired
	private CustomerService customerService;	

	@Autowired
	private FriendshipRepository friendshipRepository;
	
	@Autowired
	private AirCompanyRepository airCompanyRepository;
	
	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private TicketForFastReservationRepository ticketForFastReservationRepository;

	@Autowired
	private TicketRespository ticketRespository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private FriendInvitesRepository friendInvitesRepository;
	
	/**
	 * Kreira novu rezervaciju mesta. Kreira i uvezuje pozivnice prijateljima, ako postoje.
	 * @param reservationRequest - podaci o rezervacijama
	 * @param customer - korisnik koji kreira rezervaciju
	 * @return - kreirana (nesacuvana) rezervacija leta
	 * @throws ResourceNotFoundException - nije pronadjen let ili karta ili prijateljstvo
	 * @throws ReservationNotAvailable - neko od mesta je zauzeto
	 * @throws RequestDataException - nije poslana oznaka prijatelja, a oznaceno je da se poziva prijetelj
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public FlightReservation reserve(FlightReservationRequestDTO reservationRequest, Customer customer) throws ResourceNotFoundException, ReservationNotAvailable, RequestDataException {
		//pronadji let
		Flight flight = getFlight(reservationRequest.getFlightId());
		//kreiraj rezervaciju
		FlightReservation flightReservation = new FlightReservation(flight);
		
		//rezervisi karte
		for(TicketReservationRequestDTO ticketRequest: reservationRequest.getTicketReservations()) {
			//proveri validnost zbog imena, prezimena i pasos
			if(!ticketRequest.checkIfBasicDataIsValid()) {
				throw new RequestDataException("Data is missing.");
			}
			//pronadji kartu
			Ticket ticket = getTicket(ticketRequest.getTicketId());
			if (!ticket.getStatus().equals(TicketStatus.AVAILABLE)) {
				throw new ReservationNotAvailable("Reservation not possible, seat is already taken.");
			}
			
			//kreiraj rezevraciju
			TicketReservation ticketReservation = null;			
			if(ticketRequest.getStatus() == 0) {
				ticketReservation = new TicketReservation(customer.getFirstName(), customer.getLastName(), ticketRequest.getPassport(), ticket);
			} else if(ticketRequest.getStatus() == 1){
				ticketReservation = new TicketReservation(ticketRequest.getFirstName(), ticketRequest.getLastName(), ticketRequest.getPassport(), ticket);
			} else {
				Customer friend = findFriend(customer.getId(), ticketRequest.getFriendId());
				ticketReservation = new TicketReservation(friend.getFirstName(), friend.getLastName(), ticketRequest.getPassport(), ticket);
			}
			
			//zauzmi mesto
			ticket.setStatus(TicketStatus.RESERVED);
			ticketRespository.save(ticket);
			
			flightReservation.addTicketReservation(ticketReservation);
		}
		
		return flightReservation;
	}
	
	/**
	 * Menja aktivne rezervacije uvezane za karte.
	 * @param reservation
	 */
	public void saveActiveTickets(Reservation reservation) {
		if(reservation.getFlightReservation() != null) {
			for(TicketReservation ticketReservation: reservation.getFlightReservation().getTicketReservations()) {	
				Ticket ticket = ticketReservation.getTicket();
				ticket.setActiveReservation(ticketReservation);
				ticketRespository.save(ticket);
			}
		}
	}
	
	/**
	 * Cuva zahteve za putovanja.
	 * @param reservation
	 * @throws RequestDataException 
	 * @throws ResourceNotFoundException 
	 */
	public void saveFriendInvites(Reservation reservation, ReservationRequestDTO request, Integer customerId) throws ResourceNotFoundException, RequestDataException {
		if(reservation.getFlightReservation() != null) {			
			List<TicketReservationRequestDTO> requests = request.getFlightReservationRequest().getTicketReservations();
			List<TicketReservationRequestDTO> friendInvites = requests.stream().filter(r -> r.getStatus().equals(2)).collect(Collectors.toList());
			List<TicketReservation> ticketReservations = new ArrayList<TicketReservation>(reservation.getFlightReservation().getTicketReservations());
			for(TicketReservationRequestDTO fi:friendInvites) {
				TicketReservation ticketReservation = ticketReservations.stream().filter(tr -> tr.getTicket().getId().equals(fi.getTicketId())).findFirst().get();			
				Customer friend = findFriend(customerId, fi.getFriendId());
				FriendInvite invite = new FriendInvite(ticketReservation,friend);
				friendInvitesRepository.save(invite);
			}
		}
	}

	/**
	 * Vrsi brzu rezervaciju avio karte.
	 * 
	 * @param ticketId - oznaka karte za brzu rezervaciju.
	 * @param email    - email korisnika koji radi rezervaciju.
	 * @throws ResourceNotFoundException - ako karta nije pronadjena.
	 * @throws ReservationNotAvailable
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void fastReserve(Integer ticketId, String email, String passport) throws ResourceNotFoundException, ReservationNotAvailable {
		// pronadji kartu
		TicketForFastReservation ticketForFastReservation = findFastTicket(ticketId);

		Ticket ticket = ticketForFastReservation.getTicket();		
		// proveri zauzetost sedista
		if (!ticket.getStatus().equals(TicketStatus.FAST_RESERVATION)) {
			throw new ReservationNotAvailable("Fast reservation not possible, seat is already taken.");
		}

		// izbrisi kartu iz liste brzih rezervacija kompanije
		AirCompany company = ticketForFastReservation.getAirCompany();		
		company.getTicketForFastReservations().remove(ticketForFastReservation);
		airCompanyRepository.save(company);	

		// preuzmi kupca
		Customer customer = customerService.findCustomerByEmail(email);

		//kreiraj rezervaciju i dodaj je korisniku
		TicketReservation ticketReservation = new TicketReservation(customer.getFirstName(), customer.getLastName(), passport, ticket);
				
		//kreiraj rezervaciju leta
		FlightReservation flightReservation = new FlightReservation(ticket.getFlight());
		flightReservation.addTicketReservation(ticketReservation);
		
		//kreiraj rezervaicju
		Reservation reservation = new Reservation();
		reservation.setFlightReservation(flightReservation);
		reservation.setCustomer(customer);
		reservationRepository.save(reservation);
		
		//zauzmi sediste
		ticket.setStatus(TicketStatus.RESERVED);
		ticket.setActiveReservation(ticketReservation);
		ticketRespository.save(ticket);
	}

	/**
	 * Pronalazi kartu sa prosledjenim id.
	 * 
	 * @param ticketId - oznaka karte za brzu rezervaciju.
	 * @return - karta
	 * @throws ResourceNotFoundException - ako karta nije pronadjena
	 */
	private TicketForFastReservation findFastTicket(Integer ticketId) throws ResourceNotFoundException {
		Optional<TicketForFastReservation> ticket = ticketForFastReservationRepository.findById(ticketId);
		if (!ticket.isPresent()) {
			throw new ResourceNotFoundException(ticketId.toString(), "Ticket for fast reservation not found.");
		}
		return ticket.get();
	}
	
	
	private Flight getFlight(Integer id) throws ResourceNotFoundException {
		Optional<Flight> flight = flightRepository.findById(id);
		if (!flight.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Flight not found.");
		}
		if (!flight.get().getStatus().equals(FlightStatus.ACTIVE)) {
			throw new ResourceNotFoundException(id.toString(), "Flight not found.");
		}
		return flight.get();
	}
	
	private Ticket getTicket(Long id) throws ResourceNotFoundException {
		Optional<Ticket> ticket = ticketRespository.findById(id);
		if (!ticket.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Ticket not found.");
		}
		return ticket.get();
	}
	
	private Customer findFriend(Integer user1, Integer user2) throws ResourceNotFoundException, RequestDataException {
		//ako je status pogresno postavljen
		if(user2 == null) {
			throw new RequestDataException("Friend id missing.");
		}
		Optional<Friendship> friendshipOpt = friendshipRepository.findFriendship(user1, user2);
		if(!friendshipOpt.isPresent()) {
			throw new ResourceNotFoundException(user2.toString(), "Friend not found.");
		}
		Friendship friendship = friendshipOpt.get();
		if( friendship.getKey().getFrom().getId().equals(user1) ) {
			return friendship.getKey().getTo();
		} 
		else {
			return friendship.getKey().getFrom();
		}
	}
}
