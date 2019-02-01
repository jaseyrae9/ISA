package isa.project.controller.rentacar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.RentACarCompanyAdminCheck;
import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.rentacar.CarReservationDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.rentacar.CarService;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value = "rent_a_car_companies")
public class RentACarCompanyController {

	@Autowired
	private RentACarCompanyService rentACarCompanyService;

	@Autowired
	private CarService carService;
	
	/**
	 * Returns DTO objects for rent a car companies. Objects contain id, name,
	 * address and description.
	 * 
	 * @return information about all rent a car companies.
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllRentACarCompaniesPageable(HttpServletRequest request, Pageable page) {
		Page<RentACarCompany> companies = rentACarCompanyService.findAll(page);

		// convert companies to DTO
		List<RentACarCompanyDTO> companiesDTO = new ArrayList<>();
		for (RentACarCompany company : companies) {
			companiesDTO.add(new RentACarCompanyDTO(company));
		}

		Page<RentACarCompanyDTO> ret = new PageImpl<>(companiesDTO, companies.getPageable(), companies.getTotalElements());

		return ResponseEntity.ok(ret);
	}
	

	/**
	 * Returns DTO objects for rent a car companies. Objects contain id, name,
	 * address and description.
	 * 
	 * @return information about all rent a car companies.
	 */
	@RequestMapping(value = "/allCompanies", method = RequestMethod.GET)
	public ResponseEntity<List<RentACarCompanyDTO>> getAllRentACarCompanies() {
		Iterable<RentACarCompany> companies = rentACarCompanyService.findAll();

		// convert companies to DTO
		List<RentACarCompanyDTO> ret = new ArrayList<>();
		for (RentACarCompany company : companies) {
			ret.add(new RentACarCompanyDTO(company));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}


	/**
	 * Returns data about rent a car company with selected id.
	 * 
	 * @param id - id of rent a car company
	 * @return
	 * @throws ResourceNotFoundException - if there is no hotel with selected id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCarCompany(@PathVariable Integer id) throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(id);
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car company not found");
		}
		return new ResponseEntity<>(new RentACarCompanyDTO(carCompany.get()), HttpStatus.OK); // Maybe, DTO
	}

	/**
	 * Adds new rent a car company.
	 * 
	 * @param company
	 * @return
	 */
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RentACarCompanyDTO> addRentACarCompany(@Valid @RequestBody RentACarCompanyDTO rentACarCompanyDTO) {
		RentACarCompany company = new RentACarCompany(rentACarCompanyDTO.getName(),
				rentACarCompanyDTO.getDescription());
		company.setLocation(rentACarCompanyDTO.getLocation());
		return new ResponseEntity<>(new RentACarCompanyDTO(rentACarCompanyService.saveRentACarCompany(company)),
				HttpStatus.CREATED);
	}

	/**
	 * Edit existing rent a car company.
	 * 
	 * @param company
	 * @return
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<RentACarCompanyDTO> editRentACarCompany(@PathVariable Integer id,
			@Valid @RequestBody RentACarCompanyDTO company) {
		// rent a car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(company.getId());

		// rent a car company is not found
		if (!opt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// set name and description
		opt.ifPresent(rentACarCompany -> {
			rentACarCompany.setName(company.getName());
			rentACarCompany.setDescription(company.getDescription());
			rentACarCompany.getLocation().setAddress(company.getLocation().getAddress());
			rentACarCompany.getLocation().setCity(company.getLocation().getCity());
			rentACarCompany.getLocation().setCountry(company.getLocation().getCountry());
			rentACarCompany.getLocation().setLat(company.getLocation().getLat());
			rentACarCompany.getLocation().setLon(company.getLocation().getLon());
		});
		return new ResponseEntity<>(new RentACarCompanyDTO(rentACarCompanyService.saveRentACarCompany(opt.get())),
				HttpStatus.OK);
	}

	/**
	 * Adds new car to rent a car company.
	 * 
	 * @param car
	 * @return
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/addCar/{companyId}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<CarDTO> addCar(@PathVariable Integer companyId, @Valid @RequestBody CarDTO carDTO)
			throws ResourceNotFoundException {
		Car car = rentACarCompanyService.addCar(companyId, carDTO);
		return new ResponseEntity<>(new CarDTO(car), HttpStatus.CREATED);
	}

	/**
	 * Edit car of rent a car company.
	 * 
	 * @param car
	 * @return
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/editCar/{companyId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<CarDTO> editCar(@PathVariable Integer companyId, @Valid @RequestBody CarDTO carDTO)
			throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}

		for (Car c : carCompany.get().getCars()) {
			if (c.getId().equals(carDTO.getId())) {
				c.setBrand(carDTO.getBrand());
				c.setModel(carDTO.getModel());
				c.setType(carDTO.getType());
				c.setYearOfProduction(carDTO.getYearOfProduction());
				c.setSeatsNumber(carDTO.getSeatsNumber());
				c.setDoorsNumber(carDTO.getDoorsNumber());
				c.setPrice(carDTO.getPrice());
			}
		}

		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		return new ResponseEntity<>(carDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/deleteCar/{carCompanyId}/{carId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> deleteCar(@PathVariable Integer carCompanyId, @PathVariable Integer carId)
			throws ResourceNotFoundException {
		Optional<RentACarCompany> rentACarCompany = rentACarCompanyService.findRentACarCompany(carCompanyId);

		if (!rentACarCompany.isPresent()) {
			throw new ResourceNotFoundException(carCompanyId.toString(), "Rent a car company not found");
		}

		for (Car c : rentACarCompany.get().getCars()) {
			if (c.getId().equals(carId)) {
				c.setActive(false);
				break;
			}
		}

		rentACarCompanyService.saveRentACarCompany(rentACarCompany.get());
		return new ResponseEntity<>(carId, HttpStatus.OK);
	}

	/**
	 * Adds new branch office to rent a car company.
	 * 
	 * @param branch office
	 * @return
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/addBranchOffice/{companyId}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<BranchOfficeDTO> addBranchOffice(@PathVariable Integer companyId,
			@Valid @RequestBody BranchOfficeDTO branchOfficeDTO) throws ResourceNotFoundException {
	
		BranchOffice branchOffice = rentACarCompanyService.addBranchOffice(companyId, branchOfficeDTO);
		
		return new ResponseEntity<>(new BranchOfficeDTO(branchOffice), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/deleteBranchOffice/{carCompanyId}/{branchOfficeId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> deleteBranchOffice(@PathVariable Integer carCompanyId,
			@PathVariable Integer branchOfficeId) throws ResourceNotFoundException {
		Optional<RentACarCompany> rentACarCompany = rentACarCompanyService.findRentACarCompany(carCompanyId);

		if (!rentACarCompany.isPresent()) {
			throw new ResourceNotFoundException(carCompanyId.toString(), "Rent a car company not found");
		}

		for (BranchOffice bo : rentACarCompany.get().getBranchOffices()) {
			if (bo.getId().equals(branchOfficeId)) {
				bo.setActive(false);
				break;
			}
		}

		rentACarCompanyService.saveRentACarCompany(rentACarCompany.get());
		return new ResponseEntity<>(branchOfficeId, HttpStatus.OK);
	}

	/**
	 * Edit branch office of rent a car company.
	 * 
	 * @param companyId
	 * @param branchOfficeDTO
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/editBranchOffice/{companyId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<BranchOfficeDTO> editBranchOffice(@PathVariable Integer companyId,
			@Valid @RequestBody BranchOfficeDTO branchOfficeDTO) throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}

		for (BranchOffice bo : carCompany.get().getBranchOffices()) {
			if (bo.getId().equals(branchOfficeDTO.getId())) {
				bo.setName(branchOfficeDTO.getName());
				bo.getLocation().setAddress(branchOfficeDTO.getLocation().getAddress());
				
				bo.getLocation().setLat(branchOfficeDTO.getLocation().getLat());
				bo.getLocation().setLon(branchOfficeDTO.getLocation().getLon());
			}
		}

		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		return new ResponseEntity<>(branchOfficeDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value="/rentCar/{carCompanyId}/{pickUpBranchOfficeId}/{dropOffBranchOfficeId}/{pickUpDate}/{dropOffDate}/{carId}/{customer}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<CarReservationDTO> rentCar(@PathVariable Integer carCompanyId, @PathVariable Integer pickUpBranchOfficeId, @PathVariable Integer dropOffBranchOfficeId,
			@PathVariable String pickUpDate, @PathVariable String dropOffDate, @PathVariable Integer carId, @PathVariable String customer) throws ResourceNotFoundException, ParseException, RequestDataException{ 
	
		CarReservation carReservation = carService.addReservation(carCompanyId, carId, customer, pickUpBranchOfficeId, dropOffBranchOfficeId, pickUpDate, dropOffDate);
		
		return new ResponseEntity<>(new CarReservationDTO(carReservation), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getAllSearched/{companyName}/{companyAddress}/{pickUpDate}/{dropOffDate}", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<?> getAllSearchedCompanies(@PathVariable String companyName, @PathVariable String companyAddress,
			@PathVariable String pickUpDate, @PathVariable String dropOffDate, HttpServletRequest request, Pageable page ) throws ParseException {
		
		String carCompanyName = "";
		if(companyName.split("=").length > 1) {
			carCompanyName = companyName.split("=")[1];
		}
		
		String carCompanyAddress = "";
		if(companyAddress.split("=").length > 1) {
			carCompanyAddress = companyAddress.split("=")[1];			
		}
		
		String pickUp ="";
		if(pickUpDate.split("=").length > 1) {
			pickUp = pickUpDate.split("=")[1];
		}

		String dropOff = "";
		if(dropOffDate.split("=").length > 1) {
			dropOff = dropOffDate.split("=")[1];
		}
		
		Iterable<RentACarCompany> companies = rentACarCompanyService.searchAll(carCompanyName, carCompanyAddress, pickUp, dropOff);

		// convert companies to DTO
		List<RentACarCompanyDTO> ret = new ArrayList<>();
		for (RentACarCompany company : companies) {
			ret.add(new RentACarCompanyDTO(company));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	
	
}
