package isa.project.controller.aircompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.dto.aircompany.DestinationDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.service.aircompany.AirCompanyService;

@RestController
@RequestMapping(value = "/aircompanies")
public class AirCompanyController {

	@Autowired
	private AirCompanyService airCompanyService;

	/**
	 * Returns DTO objects for air companies. Objects contain id, name, address and
	 * description.
	 * 
	 * @return information about all air companies.
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<AirCompanyDTO>> getAllAvioCompanies() {
		Iterable<AirCompany> companies = airCompanyService.findAll();

		// convert companies to DTO
		List<AirCompanyDTO> ret = new ArrayList<>();
		for (AirCompany company : companies) {
			ret.add(new AirCompanyDTO(company));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/**
	 * Returns data about air company with selected id.
	 * 
	 * @param id - id of air company
	 * @return
	 * @throws ResourceNotFoundException - if there is no aircompany with selected
	 *                                   id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCompany(@PathVariable Integer id) throws ResourceNotFoundException {
		Optional<AirCompany> airCompany = airCompanyService.findAircompany(id);
		if (!airCompany.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found");
		}
		return new ResponseEntity<>(airCompany.get(), HttpStatus.OK);
	}

	/**
	 * Adds new air company.
	 * 
	 * @param company
	 * @return
	 */
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<AirCompanyDTO> addAvioCompany(@Valid @RequestBody AirCompanyDTO company) {
		AirCompany airCompany = new AirCompany(company.getName(), company.getDescription());
		return new ResponseEntity<>(new AirCompanyDTO(airCompanyService.saveAirCompany(airCompany)),
				HttpStatus.CREATED);
	}

	/**
	 * Edit existing air company.
	 * 
	 * @param company - contains new information for air company
	 * @return
	 * @throws ResourceNotFoundException - if air company if not found
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@RequestMapping(value = "/edit", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<AirCompanyDTO> editAvioCompany(@Valid @RequestBody AirCompanyDTO company)
			throws ResourceNotFoundException {
		// air company must exist
		Optional<AirCompany> opt = airCompanyService.findAircompany(company.getId());

		// air company is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(company.getId().toString(), "Air company not found.");
		}

		// set name and description
		opt.ifPresent(airCompany -> {
			airCompany.setName(company.getName());
			airCompany.setDescription(company.getDescription());
		});

		return new ResponseEntity<>(new AirCompanyDTO(airCompanyService.saveAirCompany(opt.get())), HttpStatus.OK);
	}

	/**
	 * Dodaje novu destinaciju na listu destinacija avio kompanije.
	 * 
	 * @param destinationDTO - informacije o destinaciji
	 * @param id - id avio kompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne pronađe avio kompanija kojoj se dodaje destinacija
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@RequestMapping(value = "/addDestination/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addDestinatin(@Valid @RequestBody DestinationDTO destinationDTO, @PathVariable Integer id)
			throws ResourceNotFoundException {
		Destination destination = airCompanyService.addDestination(destinationDTO, id);
		return new ResponseEntity<>(new DestinationDTO(destination), HttpStatus.OK);
	}
	
	/**
	 * Dodaje novu informaciju o ceni prevoza prtljaga.
	 * 
	 * @param destinationDTO - informacije o prtljagu
	 * @param id - id avio kompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne pronađe avio kompanija kojoj se dodaje destinacija
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@RequestMapping(value = "/addBaggageInformation/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addBaggageInformation(@Valid @RequestBody AdditionalService baggageInformation, @PathVariable Integer id)
			throws ResourceNotFoundException {
		AdditionalService service = airCompanyService.addBaggageInformation(baggageInformation, id);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}

}
