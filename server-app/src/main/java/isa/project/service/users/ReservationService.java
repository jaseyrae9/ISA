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
import isa.project.model.users.Customer;
import isa.project.model.users.Reservation;
import isa.project.repository.users.ReservationRepository;
import isa.project.service.aircompany.FlightReservationsService;

@Service
public class ReservationService {
	@Autowired
	private FlightReservationsService flightReservationsService;
	
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
		
		//TODO: Dodati ostale dve rezervacije
		
		if(!atLeastOneReservation) {
			throw new RequestDataException("There must be at least one sub reservation.");
		}			
		
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
