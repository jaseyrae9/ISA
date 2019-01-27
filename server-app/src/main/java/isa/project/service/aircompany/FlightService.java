package isa.project.service.aircompany;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.dto.aircompany.FlightDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Airplane;
import isa.project.model.aircompany.Airplane.AirplaneStatus;
import isa.project.model.aircompany.Destination;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.FlightDestination;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.FlightRepository;

@Service
public class FlightService {
	@Autowired
	private AirCompanyRepository airCompanyRepository;

	@Autowired
	private FlightRepository flightRepository;

	/**
	 * Pronalazi letove aviokompanije. Vrća ih sortirane po datumu poletanja.
	 * 
	 * @param id   - oznaka aviokompanije
	 * @param page - stranica
	 * @return - letovi
	 */
	public Page<Flight> getFlights(Integer id, Pageable page) {
		return flightRepository.getFlights(id, page);
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
		Flight flight = new Flight();
		flight.setAirCompany(airCompany);
		flight.setAirplane(findAirplane(airCompany, flightInfo.getAirplaneId()));
		ArrayList<FlightDestination> destinations = new ArrayList<>();
		for (Long id : flightInfo.getDestinations()) {
			FlightDestination fd = new FlightDestination();
			fd.setDestination(findDestination(airCompany, id));
			fd.setFlight(flight);
			destinations.add(fd);
		}
		flight.setDestinations(destinations);
		flight.setStartDateAndTime(flightInfo.getStartDateAndTime());
		flight.setEndDateAndTime(flightInfo.getEndDateAndTime());
		flight.setLength(flightInfo.getLength());
		flight.setMaxCarryOnBags(flightInfo.getMaxCarryOnBags());
		flight.setMaxCheckedBags(flightInfo.getMaxCheckedBags());
		flight.setAdditionalServicesAvailable(flightInfo.getAdditionalServicesAvailable());
		return flightRepository.save(flight);
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
}
