package isa.project.service.aircompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.aircompany.FlightDTO;
import isa.project.dto.aircompany.FlightSearchDTO;
import isa.project.dto.aircompany.FlightTicketsPriceChangeDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Airplane;
import isa.project.model.aircompany.Airplane.AirplaneStatus;
import isa.project.model.aircompany.Destination;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.Flight.FlightStatus;
import isa.project.model.aircompany.FlightDestination;
import isa.project.model.aircompany.Seat;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.Ticket.TicketStatus;
import isa.project.model.aircompany.TicketForFastReservation;
import isa.project.model.users.AirCompanyAdmin;
import isa.project.model.users.User;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.FlightRepository;
import isa.project.repository.aircompany.TicketForFastReservationRepository;
import isa.project.repository.users.UserRepository;

@Service
public class FlightService {
	@Autowired
	private AirCompanyRepository airCompanyRepository;
	
	@Autowired
	private TicketForFastReservationRepository ticketForFastReservationRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Pronalazi sve letove koji odgovaraju kriterijumu pretrage.
	 * @param searchData
	 * @return
	 */
	public List<Flight> findFlights(FlightSearchDTO searchData) {
		String departure = searchData.getDepartureAirport().toLowerCase();
		String arrival = searchData.getArrivalAirport().toLowerCase();
		List<Flight> flights = flightRepository.getFlights(
				searchData.getStart(), searchData.getEnd(),
				searchData.getNumberOfPeople(), 
				searchData.getMinPrice(), searchData.getMaxPrice());
		return flights.stream().filter(f -> checkFlight(f, departure, arrival)).collect(Collectors.toList());
	}
	
	private boolean checkFlight(Flight flight, String departureAirport, String arrivalAirport) {
		boolean checkDeparture = checkDestination(flight.getDestinations().get(0).getDestination(), departureAirport);
		boolean checkArrival = checkDestination(flight.getDestinations().get(flight.getDestinations().size()-1).getDestination(), arrivalAirport);
		if(checkDeparture && checkArrival) {
			return true;
		}
		return false;
	}
	
	private boolean checkDestination(Destination destination, String airportName) {
		return destination.getAirportName().toLowerCase().contains(airportName);
	}
	
	/**
	 * Vraća informacije o letu. Admin aviokompanije moze da dobije informacije o
	 * in_progress letu svoje aviokompanije, svi ostali samo aktivni let.
	 * 
	 * @param id    - oznaka leta
	 * @param email - email, ako se radi o ulogovanom korisniku, null za
	 *              neulogovanog
	 * @return - let
	 * @throws ResourceNotFoundException - ako let nije pronađen ili korisnik ne
	 *                                   može da mu pristupi
	 */
	public Flight getFlight(Integer id, String email) throws ResourceNotFoundException {
		Flight flight = findFlight(id);

		// svi mogu da vide aktivne letove
		if (flight.getStatus().equals(FlightStatus.ACTIVE)) {
			return flight;
		}

		// Ovde se ulazi kada je let in progress

		// neulogani korisnik sigurno ne moze da ga vidi
		if (email == null) {
			throw new ResourceNotFoundException(id.toString(), "Flight not found.");
		}

		User user = userRepository.findByEmail(email).get();

		// ako avio admin trazi za svoju kompaniju vrati mu let
		if (user instanceof AirCompanyAdmin) {
			AirCompanyAdmin airadmin = (AirCompanyAdmin) user;
			if (airadmin.getAirCompany().getId().equals(flight.getAirCompany().getId())) {
				return flight;
			}
		}

		// svi ostali ne smeju da vide
		throw new ResourceNotFoundException(id.toString(), "Flight not found.");
	}

	/**
	 * Vraća letove. Admin aviokompanije za svoju aviokompaniju dobija i aktivne i
	 * in progress letove. Svi ostali dobijaju samo aktivne. Vrća ih sortirane po
	 * datumu poletanja.
	 * 
	 * @param id    - oznaka aviokompanije
	 * @param page  - stranica
	 * @param email - email, ako se radi o ulogovanom korisniku, null za
	 *              neulogovanog
	 * @return - letovi
	 */
	public Page<Flight> getFlights(Integer id, Pageable page, String email) {
		if (email == null) {
			// neulogovanom korisniku vratiti samo aktivne
			return flightRepository.getActiveFlights(id, page);
		}
		User user = userRepository.findByEmail(email).get();

		// ako avio admin trazi za svoju kompaniju vrati mu sve
		if (user instanceof AirCompanyAdmin) {
			AirCompanyAdmin airadmin = (AirCompanyAdmin) user;
			if (airadmin.getAirCompany().getId().equals(id)) {
				return flightRepository.getFlights(id, page);
			}
		}

		// inace vrati samo aktivne
		return flightRepository.getActiveFlights(id, page);
	}

	/**
	 * Kreira novi let.
	 * 
	 * @param airCompanyId - oznaka aviokompanije
	 * @param flightInfo   - informacije o letu
	 * @return - kreirani let
	 * @throws ResourceNotFoundException - ako se ne pronađu aviokompanija, avion
	 *                                   ili destinacije
	 * @throws RequestDataException      - ako je destinacija izbrisana, ili avion
	 *                                   nije aktivan
	 */
	public Flight addNewFlight(Integer airCompanyId, FlightDTO flightInfo)
			throws ResourceNotFoundException, RequestDataException {
		checkDates(flightInfo);
		AirCompany airCompany = findAirCompany(airCompanyId);
		Flight flight = new Flight(flightInfo);
		flight.setAirCompany(airCompany);
		flight.setAirplane(findAirplane(airCompany, flightInfo.getAirplaneId()));
		ArrayList<Destination> destinations = new ArrayList<>();
		for (Long id : flightInfo.getDestinations()) {
			destinations.add(findDestination(airCompany, id));
		}
		setDestinations(flight, destinations);
		setTickets(flight, flightInfo);
		return flightRepository.save(flight);
	}
	
	/**
	 * Uredjuje letove.
	 * @param airCompanyId - oznaka aviokompanije.
	 * @param flightId - oznaka leta
	 * @param flightInfo - detalji o letu
	 * @return - uredjeni let
	 * @throws ResourceNotFoundException - ako se ne nadju aviokompanija, let, itd..
	 * @throws RequestDataException - datumi pogresni, status nije in_progress
	 */
	public Flight editFlight(Integer airCompanyId, Integer flightId, FlightDTO flightInfo) throws ResourceNotFoundException, RequestDataException {
		checkDates(flightInfo);
		Flight flight = findFlight(flightId);
		if (!flight.getAirCompany().getId().equals(airCompanyId)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}
		if (!flight.getStatus().equals(FlightStatus.IN_PROGRESS)) {
			throw new RequestDataException("You can only edit flight when their status is in progress.");
		}
		AirCompany airCompany = findAirCompany(airCompanyId);
		flight.setAirplane(findAirplane(airCompany, flightInfo.getAirplaneId()));
		flight.setData(flightInfo);
		ArrayList<Destination> destinations = new ArrayList<>();
		for (Long id : flightInfo.getDestinations()) {
			destinations.add(findDestination(airCompany, id));
		}
		setDestinations(flight, destinations);
		setTickets(flight, flightInfo);
		return flightRepository.save(flight);
	}
	

	/**
	 * Menja status leta u izabrani. (brisanje i aktiviranje leta)
	 * 
	 * @param aircompanyId - oznaka aviokompanije.
	 * @param flightId     - oznaka leta
	 * @throws ResourceNotFoundException - ako let nije pronađen, ili admin nije
	 *                                   zadužen za letove te aviokompanije
	 * @throws RequestDataException      - ako let nije u statusu IN_PROGRESS
	 */
	public Flight changeStatus(Integer aircompanyId, Integer flightId, FlightStatus status)
			throws ResourceNotFoundException, RequestDataException {
		Flight flight = findFlight(flightId);

		if (!flight.getAirCompany().getId().equals(aircompanyId)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}

		if (!flight.getStatus().equals(FlightStatus.IN_PROGRESS)) {
			throw new RequestDataException(
					"Flight you are trying to delete is not in status IN_PROGRESS and can not be delete.");
		}

		flight.setStatus(status);
		return flightRepository.save(flight);
	}
	
	/**
	 * Onemogućava sedišta za rezervaciju u tom letu. (Kao zamena za brisanje sedišta).
	 * @param aircompanyId - oznaka aviokompanije
	 * @param flightId - oznaka leta
	 * @param tickets - oznaka jedne ili više karti
	 * @return - izmenjeni let
	 * @throws RequestDataException - sedište nije slobodno
	 * @throws ResourceNotFoundException - ako resurs nije pronađen
	 */
	@Transactional(readOnly = false, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Flight disableSeats(Integer aircompanyId, Integer flightId, List<Long> tickets) throws RequestDataException, ResourceNotFoundException {
				
		Flight flight = findFlight(flightId);

		if (!flight.getAirCompany().getId().equals(aircompanyId)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}
		
		//mark all seats as AVAILABLE if they were UNAVIABLE 
		for(int i = 0; i < flight.ticketsSize(); ++i) {
			Ticket ticket = flight.getTicket(i);
			if(ticket.getStatus() == TicketStatus.UNAVIABLE) {
				ticket.setStatus(TicketStatus.AVAILABLE);
			}
		}

		//mark desired seats as UNAVIABLE 
		for(Long id: tickets) {
			Optional<Ticket> ticketOpt = flight.getTicketById(id);
			if(!ticketOpt.isPresent()) {
				throw new ResourceNotFoundException(id.toString(), "Ticket not found.");
			}
			Ticket ticket = ticketOpt.get();
			if(!ticket.getStatus().equals(TicketStatus.AVAILABLE)) {
				throw new RequestDataException("Ticket must be available to become unavailable.");
			}
			ticket.setStatus(TicketStatus.UNAVIABLE);
		}
		
		return flightRepository.save(flight);
	}
	
	/**
	 * Kreira karte namenje za brzu rezervaciju.
	 * @param aircompanyId - oznaka aviokompanije
	 * @param flightId - oznaka leta
	 * @param tickets - oznake karti
	 * @param discount - popust
	 * @return - let sa izmenjenim kartama
	 * @throws ResourceNotFoundException - resursi nisu pronadjeni
	 * @throws RequestDataException - let nije aktivan
	 */
	@Transactional(readOnly = false, propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Flight createTicketsForFastReservations(Integer aircompanyId, Integer flightId, List<Long> tickets, Double discount) throws ResourceNotFoundException, RequestDataException {
		//pronadji let
		Flight flight = findFlight(flightId);

		if (!flight.getAirCompany().getId().equals(aircompanyId)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}

		if (!flight.getStatus().equals(FlightStatus.ACTIVE)) {
			throw new RequestDataException(
					"Tickets for fast reservations can only be created for active flights.");
		}
		
		//pronadji aviokompaniju
		AirCompany airCompany = findAirCompany(aircompanyId);
		
		//kreiraj karte
		for(Long id: tickets) {
			Optional<Ticket> ticketOpt = flight.getTicketById(id);
			if(!ticketOpt.isPresent()) {
				throw new ResourceNotFoundException(id.toString(), "Ticket not found.");
			}
			Ticket ticket = ticketOpt.get();
			if(!ticket.getStatus().equals(TicketStatus.AVAILABLE)) {
				throw new RequestDataException("Ticket must be available to become fast reservation ticket.");
			}
			ticket.setStatus(TicketStatus.FAST_RESERVATION);
			ticket.setDiscount(discount);
			TicketForFastReservation tffr = new TicketForFastReservation(airCompany, ticket);
			airCompany.getTicketForFastReservations().add(tffr);
		}
		
		//sacuvaj sve
		airCompanyRepository.save(airCompany);
		return flightRepository.save(flight);		
	}
	
	/**
	 * Brise kartu za rezervaciju. Time mesto postaje slobodno.
	 * @param aircompanyId - oznaka aviokompanije.
	 * @param fastReservationId - oznaka karte za brzu rezervaciju.
	 * @throws RequestDataException - karta ne pripada datoj aviokompaniji.
	 * @throws ResourceNotFoundException - ako karta ili aviokompanija nisu pronadjeni.
	 */
	public void deleteTicketForFastReservation(Integer aircompanyId, Integer fastReservationId) throws RequestDataException, ResourceNotFoundException {
		//pronadji kartu
		TicketForFastReservation ticket = findTicketForFastResrvation(fastReservationId);
		if(!ticket.getAirCompany().getId().equals(aircompanyId)) {
			throw new RequestDataException("Ticket does not belong to your aircompany.");
		}
		
		//oslobodi sediste
		ticket.getTicket().setStatus(TicketStatus.AVAILABLE);
		
		//pronadji aviokompaniju
		AirCompany airCompany = findAirCompany(aircompanyId);
		//brisi kartu
		airCompany.removeTicket(ticket);
		//sacuvaj da vise nema karte
		airCompanyRepository.save(airCompany);
		//sacuvaj oslobodjeno mesto
		flightRepository.save(ticket.getTicket().getFlight());
	}
	
	/**
	 * Vraca sve karte namenjene za brzu rezervaciju koji pripadaju zadatoj kompaniji.
	 * @param aircompanyId - oznaka kompanije
	 * @return - karte za brzu rezervaciju
	 * @throws ResourceNotFoundException - ako kompanija nije pronadjena
	 */
	public List<TicketForFastReservation> getTicketsForFastResrevation(Integer aircompanyId) throws ResourceNotFoundException{
		AirCompany airCompany = findAirCompany(aircompanyId);
		return airCompany.getTicketForFastReservations();
	}
	
	
	/**
	 * Pronalazi kartu za brzu rezevrvaciju.
	 * @param fastReservationId - oznaka karte
	 * @return - karta
	 * @throws ResourceNotFoundException - ako karta nije pronadjena
	 */
	private TicketForFastReservation findTicketForFastResrvation(Integer fastReservationId) throws ResourceNotFoundException {
		Optional<TicketForFastReservation> ticketOpt = ticketForFastReservationRepository.findById(fastReservationId);
		if(!ticketOpt.isPresent()) {
			throw new ResourceNotFoundException(fastReservationId.toString(), "Ticket for fast reservation not found.");
		}
		return ticketOpt.get();
	}
	
	/**
	 * Promena cena leta. Moguća za aktivne i in_progress letove. Cene se postavljaju samo za mesta koja nisu 
	 * rezervisana.
	 * @param airCompanyId - oznaka aviokomapnije
	 * @param flightId - oznaka leta
	 * @param info - nove cene
	 * @return - let sa izmenjenim kartama
	 * @throws ResourceNotFoundException - ako resurs nije pronađen
	 */
	public Flight setTicketsPrices(Integer airCompanyId, Integer flightId, FlightTicketsPriceChangeDTO info) throws ResourceNotFoundException {
		Flight flight = findFlight(flightId);

		if (!flight.getAirCompany().getId().equals(airCompanyId)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}
		
		if(flight.getStatus().equals(FlightStatus.DELETED)) {
			throw new ResourceNotFoundException(flightId.toString(), "Your company does not have flight with this id.");
		}
		
		flight.setEconomyPrice(info.getEconomyPrice());
		flight.setPremiumEconomyPrice(info.getPremiumEconomyPrice());
		flight.setBussinessPrice(info.getBussinessPrice());
		flight.setFirstPrice(info.getFirstPrice());
		
		for(int i = 0; i < flight.ticketsSize(); ++i) {
			Ticket t = flight.getTicket(i);
			if(!t.getStatus().equals(TicketStatus.RESERVED)) {
				Double newPrice = info.getPriceForClass(t.getSeat().getSeatClass());
				t.setPrice(newPrice);
			}
		}
		
		return flightRepository.save(flight);
	}
	
	/**
	 * Dodaje, uklanja i edituje destinacije u letu. 
	 * @param flight - let
	 * @param destinations - destinacije
	 */
	private void setDestinations(Flight flight, List<Destination> destinations) {
		for(int i = 0; i < destinations.size(); ++i) {
			if( i > flight.getDestinations().size() - 1) {
				//kreiraj novu
				FlightDestination fd = new FlightDestination();
				fd.setDestination(destinations.get(i));
				fd.setFlight(flight);
				fd.setIndex(i);
				flight.addDestination(fd);
			}
			else {
				//uredi postojecu destinaciju
				FlightDestination fd = flight.getDestinations().get(i);
				fd.setDestination(destinations.get(i));	
				fd.setIndex(i);
			}
		}
		
		// obrisi viska destinacije
		if (destinations.size() < flight.getDestinations().size()) {
			flight.removeDestinationsStartingFromIndex(destinations.size());
		}
	}
	

	/**
	 * Ispravlja karte u letu.
	 * 
	 * @param flight - let
	 * @param info - informacije iz dto
	 */
	private void setTickets(Flight flight, FlightDTO info) {
		// za svako sediste namesti kartu
		for (int i = 0; i < flight.getAirplane().getSeats().size(); ++i) {
			if (i > flight.ticketsSize() - 1) {
				// nedostaje karta, treba dodati novu
				Seat seat  = flight.getAirplane().getSeats().get(i);
				Double price = info.getPriceForClass(seat.getSeatClass());
				Ticket ticket = new Ticket(flight, seat, price);
				ticket.setIndex(i);
				flight.addTicket(ticket);
			} else {
				// postoji karta, samo je izmeni
				Ticket ticket = flight.getTicket(i);
				if (!ticket.getStatus().equals(TicketStatus.RESERVED)) {
					Seat seat  = flight.getAirplane().getSeats().get(i);
					Double price = info.getPriceForClass(seat.getSeatClass());
					ticket.setSeat(seat);
					ticket.setPrice(price);
					ticket.setIndex(i);
				}
			}
		}

		// obrisi viska karte ako postoje
		if (flight.getAirplane().getSeats().size() < flight.ticketsSize()) {
			flight.removeTicketsStartingFromIndex(flight.getAirplane().getSeats().size());
		}
	}

	/**
	 * Provera da li je vreme dolaska posle vremena polaska.
	 * 
	 * @param info - informacije o letu
	 * @throws RequestDataException - ako je vreme dolaska pre vremena polaska
	 */
	private void checkDates(FlightDTO info) throws RequestDataException {
		if (info.getStartDateAndTime().compareTo(info.getEndDateAndTime()) >= 0) {
			throw new RequestDataException("Departure date must be before arrival date.");
		}
	}

	/**
	 * Pronalazi aviokompaniju sa datom oznakom.
	 * 
	 * @param id - oznaka aviokompanije
	 * @return - aviokompanija
	 * @throws ResourceNotFoundException - ako nije pronađena
	 */
	private AirCompany findAirCompany(Integer id) throws ResourceNotFoundException {
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		return airCompanyOpt.get();
	}

	/**
	 * Pronalazi avion.
	 * 
	 * @param airCompany - aviokompanija
	 * @param airplaneId - oznaka aviona
	 * @return - avion
	 * @throws ResourceNotFoundException - ako avion nije nađen
	 * @throws RequestDataException      - ako avion sa datom oznakom nije moguće
	 *                                   koristiti, jer nije aktivan
	 */
	private Airplane findAirplane(AirCompany airCompany, Integer airplaneId)
			throws ResourceNotFoundException, RequestDataException {
		Optional<Airplane> airplane = airCompany.getAirplanes().stream().filter(a -> a.getId().equals(airplaneId))
				.findFirst();
		if (!airplane.isPresent()) {
			throw new ResourceNotFoundException(airplaneId.toString(), "Airplane not found.");
		}
		if (!airplane.get().getStatus().equals(AirplaneStatus.ACTIVE)) {
			throw new RequestDataException("Airplane is not active and can not be used.");
		}
		return airplane.get();
	}

	/**
	 * Pronalazi destinaciju.
	 * 
	 * @param airCompany    - aviokompanija kojoj pripada destinacija.
	 * @param destinationID - oznaka destinacije.
	 * @return - destinacija.
	 * @throws ResourceNotFoundException - ako se ne pronađe destinacija sa datom
	 *                                   oznakom.
	 * @throws RequestDataException      - ako je destinacija sa datom oznakom
	 *                                   izbrisana.
	 */
	private Destination findDestination(AirCompany airCompany, Long destinationID)
			throws ResourceNotFoundException, RequestDataException {
		Optional<Destination> destination = airCompany.getDestinations().stream()
				.filter(d -> d.getId().equals(destinationID)).findFirst();
		if (!destination.isPresent()) {
			throw new RequestDataException("Destination you are trying to use is not found.");
		}
		if (!destination.get().getActive()) {
			throw new RequestDataException("Destination you are trying to use is not found.");
		}
		return destination.get();
	}

	/**
	 * Pronalazi let (aktivni ili pasivni).
	 * 
	 * @param id - oznaka leta
	 * @return - let
	 * @throws ResourceNotFoundException - ako let nije pronađen.
	 */
	private Flight findFlight(Integer id) throws ResourceNotFoundException {
		Optional<Flight> flightOpt = flightRepository.findById(id);
		if (!flightOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Flight not found.");
		}
		Flight flight = flightOpt.get();
		if (flight.getStatus().equals(FlightStatus.DELETED)) {
			throw new ResourceNotFoundException(id.toString(), "Flight not found.");
		}
		return flight;
	}
}
