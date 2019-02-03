package isa.project.service.aircompany;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.Ticket.TicketStatus;
import isa.project.model.aircompany.TicketForFastReservation;
import isa.project.model.aircompany.TicketReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.Reservation;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.TicketForFastReservationRepository;
import isa.project.repository.aircompany.TicketRespository;
import isa.project.repository.users.CustomerRepository;
import isa.project.repository.users.ReservationRepository;

@Service
public class FlightReservationsService {
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AirCompanyRepository airCompanyRepository;

	@Autowired
	private TicketForFastReservationRepository ticketForFastReservationRepository;

	@Autowired
	private TicketRespository ticketRespository;
	
	@Autowired
	private ReservationRepository reservationRepository;

	/**
	 * Vrsi brzu rezervaciju avio karte.
	 * 
	 * @param ticketId - oznaka karte za brzu rezervaciju.
	 * @param email    - email korisnika koji radi rezervaciju.
	 * @throws ResourceNotFoundException - ako karta nije pronadjena.
	 * @throws ReservationNotAvailable
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor= {ResourceNotFoundException.class})
	public void fastReserve(Integer ticketId, String email, String passport) throws ResourceNotFoundException, ReservationNotAvailable {
		// pronadji kartu
		TicketForFastReservation ticketForFastReservation = findFastTicket(ticketId);

		Ticket ticket = ticketForFastReservation.getTicket();		
		// proveri zauzetost sedista
		if (!ticket.getStatus().equals(TicketStatus.FAST_RESERVATION)) {
			throw new ReservationNotAvailable("Fast reservation not possible, seat is already taken.");
		}

		// zauzmi sediste
		ticket.setStatus(TicketStatus.RESERVED);
		// sacuvaj izmene
		ticketRespository.save(ticket);

		// izbrisi kartu iz liste brzih rezervacija kompanije
		AirCompany company = ticketForFastReservation.getAirCompany();		
		company.getTicketForFastReservations().remove(ticketForFastReservation);
		airCompanyRepository.save(company);	

		// preuzmi kupca
		Customer customer = customerRepository.findByEmail(email).get();

		//kreiraj rezervaciju i dodaj je korisniku
		TicketReservation ticketReservation = new TicketReservation(customer.getFirstName(), customer.getLastName(), passport, ticket);
		FlightReservation flightReservation = new FlightReservation(ticket.getFlight());
		flightReservation.addTicketReservation(ticketReservation);
		Reservation reservation = new Reservation();
		reservation.setFlightReservation(flightReservation);
		reservation.setCustomer(customer);
		reservationRepository.save(reservation);		
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

}
