package isa.project.service.aircompany;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.aircompany.DestinationDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.aircompany.AirCompanyRepository;

@Service
public class AirCompanyService {
	@Autowired
	private AirCompanyRepository airCompanyRespository;

	public Iterable<AirCompany> findAll() {
		return airCompanyRespository.findAll();
	}

	public Optional<AirCompany> findAircompany(Integer id) {
		return airCompanyRespository.findById(id);
	}

	public AirCompany saveAirCompany(AirCompany company) {
		return airCompanyRespository.save(company);
	}

	/**
	 * Dodaje novu destinaciju na koju avio kompanija putuje.
	 * 
	 * @param destinationDTO - informacije o destinaciji
	 * @param id             - id aviokompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe avio kompanija
	 */
	public Destination addDestination(DestinationDTO destinationDTO, Integer id) throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje destinacija
		Optional<AirCompany> airCompanyOpt = airCompanyRespository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// kreiraj destinaciju
		Destination destination = new Destination(airCompany, destinationDTO.getLabel(), destinationDTO.getCountry(),
				destinationDTO.getAirportName());

		// sacuvaj destinaciju u avio komapniji
		airCompany.getDestinations().add(destination);
		airCompanyRespository.save(airCompany);

		return airCompany.getDestinations().get(airCompany.getDestinations().size() - 1);
	}

	/**
	 * Dodaje novu informaciju o prtljagu u aviokompaniju.
	 * 
	 * @param baggegeInformation - informacije o prtljagu
	 * @param id                 - id aviokompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe avio kompanija
	 */
	public AdditionalService addBaggageInformation(AdditionalService baggageInformation, Integer id)
			throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje destinacija
		Optional<AirCompany> airCompanyOpt = airCompanyRespository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// sacuvaj informaciju o prtljagu u aviokompaniji
		airCompany.getBaggageInformation().add(baggageInformation);
		airCompanyRespository.save(airCompany);

		return airCompany.getBaggageInformation().get(airCompany.getBaggageInformation().size() - 1);
	}

}
