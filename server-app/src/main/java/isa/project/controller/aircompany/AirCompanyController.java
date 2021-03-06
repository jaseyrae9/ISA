package isa.project.controller.aircompany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.AirCompanyAdminCheck;
import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.dto.aircompany.AirCompanyFullDetails;
import isa.project.dto.aircompany.DestinationDTO;
import isa.project.exception_handlers.RequestDataException;
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
	
	@RequestMapping(value = "/allAirCompanies", method = RequestMethod.GET)
	public ResponseEntity<?> getAllAvioCompaniesPage(HttpServletRequest request, Pageable page) {
		Page<AirCompany> companies = airCompanyService.findAll(page);

		// convert companies to DTO
		List<AirCompanyDTO> companiesDTO = new ArrayList<>();
		for (AirCompany company : companies) {
			companiesDTO.add(new AirCompanyDTO(company));
		}

		Page<AirCompanyDTO> ret = new PageImpl<>(companiesDTO, companies.getPageable(), companies.getTotalElements());
		return ResponseEntity.ok(ret);
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
		return new ResponseEntity<>(new AirCompanyFullDetails(airCompany.get()), HttpStatus.OK);
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
		airCompany.setLocation(company.getLocation());
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
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<AirCompanyDTO> editAvioCompany(@PathVariable Integer id, @Valid @RequestBody AirCompanyDTO company)
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
			airCompany.getLocation().setAddress(company.getLocation().getAddress());
			airCompany.getLocation().setCity(company.getLocation().getCity());
			airCompany.getLocation().setCountry(company.getLocation().getCountry());
			airCompany.getLocation().setLat(company.getLocation().getLat());
			airCompany.getLocation().setLon(company.getLocation().getLon());
		});

		return new ResponseEntity<>(new AirCompanyDTO(airCompanyService.saveAirCompany(opt.get())), HttpStatus.OK);
	}

	/**
	 * Dodaje novu destinaciju na listu destinacija avio kompanije.
	 * 
	 * @param destinationDTO - informacije o destinaciji
	 * @param id             - id avio kompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne pronađe avio kompanija kojoj se
	 *                                   dodaje destinacija
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/addDestination/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addDestinatin(@PathVariable Integer id, @Valid @RequestBody DestinationDTO destinationDTO)
			throws ResourceNotFoundException {
		Destination destination = airCompanyService.addDestination(destinationDTO, id);
		return new ResponseEntity<>(new DestinationDTO(destination), HttpStatus.OK);
	}
	
	/**
	 * Briše (logički) destinaciju.
	 * 
	 * @param id - oznaka aviokomapnije
	 * @param destination - oznaka destinacije
	 * @throws ResourceNotFoundException - ako nisu pronađeni aviokompanija ili destinacija
	 * @throws RequestDataException - ako je destinaija izbrisana
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/deleteDestination/{id}/{destination}", method = RequestMethod.DELETE)
	public ResponseEntity<?> editDestination(@PathVariable Integer id, @PathVariable Long destination) throws ResourceNotFoundException, RequestDataException{
		airCompanyService.deleteDestination(destination, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Menja postojeću destinaciju.
	 * 
	 * @param id - oznaka aviokomapnije
	 * @param destinationDTO - informacije o destinaciji
	 * @return - destinacija
	 * @throws ResourceNotFoundException - ako nisu pronađeni aerodrum ili destinacija
	 * @throws RequestDataException - ako je destinaija izbrisana
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/editDestination/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> editDestination(@PathVariable Integer id, @Valid @RequestBody DestinationDTO destinationDTO) throws ResourceNotFoundException, RequestDataException{
		Destination destination = airCompanyService.editDestination(destinationDTO.getId(),id, destinationDTO);
		return new ResponseEntity<>(new DestinationDTO(destination), HttpStatus.OK);
	}

	/**
	 * Dodaje novu informaciju o ceni prevoza prtljaga.
	 * 
	 * @param AdditionalService - informacije o prtljagu
	 * @param id             - id avio kompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne pronađe avio kompanija kojoj se
	 *                                   dodaje destinacija
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/addBaggageInformation/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addBaggageInformation(
			@PathVariable Integer id, @Valid @RequestBody AdditionalService baggageInformation) throws ResourceNotFoundException {
		AdditionalService service = airCompanyService.addBaggageInformation(baggageInformation, id);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}
	
	/**
	 * Briše informaciju o prtljagu.
	 * @param id - oznaka aviokompanije
	 * @param baggageId - oznaka informacije o prtljagu
	 * @return
	 * @throws ResourceNotFoundException - ako nisu pronađeni informacija o prtljagu, ili aviokompanija
	 * @throws RequestDataException - ako je informacija već izbrisana
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/deleteBaggageInformation/{id}/{baggageId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBaggageInformation(@PathVariable Integer id, @PathVariable Long baggageId) throws ResourceNotFoundException, RequestDataException{
		airCompanyService.deleteBaggageInformation(baggageId, id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Menja postojeću informaciju o prtljagu.
	 * 
	 * @param id - oznaka aviokomapnije
	 * @param baggaeInfo - informacije o prtljagu
	 * @return - izmenjena destinacija o prtljagu
	 * @throws ResourceNotFoundException - ako nisu pronađeni aerodrum ili informacija o prtljagu
	 * @throws RequestDataException - ako je informacija o prtljagu izbrisana
	 */
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/editBaggageInformation/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> editBaggageInformation(@PathVariable Integer id, @Valid @RequestBody AdditionalService baggageInfo) throws ResourceNotFoundException, RequestDataException{
		AdditionalService baggageInfomation = airCompanyService.editBaggageInformation(baggageInfo,  id);
		return new ResponseEntity<>(baggageInfomation, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/getSoldTicketsPerMonth/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSoldTicketsPerMonth(@PathVariable Integer id){
		return new ResponseEntity<>(airCompanyService.getSoldTicketsPerMonth(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/getSoldTicketsPerDay/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSoldTicketsPerDay(@PathVariable Integer id){
		return new ResponseEntity<>(airCompanyService.getSoldTicketsPerDay(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/getSoldTicketsPerWeek/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSoldTicketsPerWeek(@PathVariable Integer id){
		return new ResponseEntity<>(airCompanyService.getSoldTicketsPerWeek(id), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	@RequestMapping(value = "/getProfitInPeriod/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProfitInPeriod(@PathVariable Integer id, @RequestParam @DateTimeFormat(pattern="MM-dd-yyyy") Date from, @RequestParam @DateTimeFormat(pattern="MM-dd-yyyy") Date to){
		return new ResponseEntity<>(airCompanyService.getProfitInPeriod(id, from, to), HttpStatus.OK);
	}
}
