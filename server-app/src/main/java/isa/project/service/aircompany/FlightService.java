package isa.project.service.aircompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.dto.aircompany.FlightDTO;
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
import isa.project.model.users.AirCompanyAdmin;
import isa.project.model.users.User;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.FlightRepository;
import isa.project.repository.users.UserRepository;

@Service
public class FlightService {
	@Autowired
	private AirCompanyRepository airCompanyRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private UserRepository userRepository;

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
				flight.addDestination(fd);
			}
			else {
				//uredi postojecu destinaciju
				FlightDestination fd = flight.getDestinations().get(i);
				fd.setDestination(destinations.get(i));				
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
				flight.addTicket(ticket);
			} else {
				// postoji karta, samo je izmeni
				Ticket ticket = flight.getTicket(i);
				if (!ticket.getStatus().equals(TicketStatus.RESERVED)) {
					Seat seat  = flight.getAirplane().getSeats().get(i);
					Double price = info.getPriceForClass(seat.getSeatClass());
					ticket.setSeat(seat);
					ticket.setPrice(price);
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
			throw new ResourceNotFoundException(destinationID.toString(), "Destination not found.");
		}
		if (!destination.get().getActive()) {
			throw new RequestDataException("Destination is deleted.");
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
