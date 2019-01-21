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
import isa.project.repository.aircompany.DestinationRepository;
import isa.project.repository.shared.AdditionalServiceRepository;

@Service
public class AirCompanyService {
	@Autowired
	private AirCompanyRepository airCompanyRepository;
	@Autowired
	private DestinationRepository destinationRepository;	
	@Autowired
	private AdditionalServiceRepository additionalServiceRepository;

	public Iterable<AirCompany> findAll() {
		return airCompanyRepository.findAll();
	}

	public Optional<AirCompany> findAircompany(Integer id) {
		return airCompanyRepository.findById(id);
	}

	public AirCompany saveAirCompany(AirCompany company) {
		return airCompanyRepository.save(company);
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
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// kreiraj destinaciju
		Destination destination = new Destination(airCompany, destinationDTO.getLabel(), destinationDTO.getCountry(),
				destinationDTO.getAirportName());

		// sacuvaj 
		return destinationRepository.save(destination);
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
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();
		
		//sacuvaj dodatnu uslugu
		AdditionalService service = additionalServiceRepository.save(baggageInformation);

		// sacuvaj informaciju o prtljagu u aviokompaniji
		airCompany.getBaggageInformation().add(baggageInformation);
		airCompanyRepository.save(airCompany);

		return service;
	}

}
