package isa.project.controller.rentacar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.RentACarCompanyAdminCheck;
import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.users.Customer;
import isa.project.security.TokenUtils;
import isa.project.service.rentacar.CarReservationService;
import isa.project.service.rentacar.CarService;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value = "rent_a_car_companies")
public class RentACarCompanyController {

	@Autowired
	private RentACarCompanyService rentACarCompanyService;

	@Autowired
	private CarService carService;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private CarReservationService carReservationService;

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

		Page<RentACarCompanyDTO> ret = new PageImpl<>(companiesDTO, companies.getPageable(),
				companies.getTotalElements());

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
	public ResponseEntity<RentACarCompanyDTO> addRentACarCompany(
			@Valid @RequestBody RentACarCompanyDTO rentACarCompanyDTO) {
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
	 * @throws RequestDataException
	 */
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/editCar/{companyId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<CarDTO> editCar(@PathVariable Integer companyId, @Valid @RequestBody CarDTO carDTO)
			throws ResourceNotFoundException, RequestDataException {
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}

		CarDTO newCarDTO = null;
		for (Car c : carCompany.get().getCars()) {
			if (c.getId().equals(carDTO.getId())) {

				if (notReserved(c)) {
					c.setBrand(carDTO.getBrand());
					c.setModel(carDTO.getModel());
					c.setType(carDTO.getType());
					c.setYearOfProduction(carDTO.getYearOfProduction());
					c.setSeatsNumber(carDTO.getSeatsNumber());
					c.setDoorsNumber(carDTO.getDoorsNumber());
					c.setPrice(carDTO.getPrice());
					newCarDTO = new CarDTO(c);
					break;
				} else {
					throw new RequestDataException("Can't edit reserved car!");
				}
			}
		}

		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		return new ResponseEntity<>(newCarDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/deleteCar/{carCompanyId}/{carId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> deleteCar(@PathVariable Integer carCompanyId, @PathVariable Integer carId)
			throws ResourceNotFoundException, RequestDataException {
		Optional<RentACarCompany> rentACarCompany = rentACarCompanyService.findRentACarCompany(carCompanyId);

		if (!rentACarCompany.isPresent()) {
			throw new ResourceNotFoundException(carCompanyId.toString(), "Rent a car company not found");
		}

		for (Car c : rentACarCompany.get().getCars()) {
			if (c.getId().equals(carId)) {
				if (notReserved(c)) {
					c.setActive(false);
					break;
				} else {
					throw new RequestDataException("Can't delete reserved car!");
				}
			}
		}

		rentACarCompanyService.saveRentACarCompany(rentACarCompany.get());
		return new ResponseEntity<>(carId, HttpStatus.OK);
	}

	public Boolean notReserved(Car car) {
		Date today = new Date();
		for (CarReservation c : car.getCarReservations()) {
			if (c.getActive() && today.compareTo(c.getDropOffDate()) < 0) {
				return false;
			}
		}
		return true;
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
	@RequestMapping(value = "/deleteBranchOffice/{carCompanyId}/{branchOfficeId}", method = RequestMethod.DELETE)
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

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> getAllSearchedCompanies(@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String address, @RequestParam(defaultValue = "") String pickUp,
			@RequestParam(defaultValue = "") String dropOff) throws ParseException {

		Iterable<RentACarCompany> companies = rentACarCompanyService.searchAll(name, address, pickUp, dropOff);

		// convert companies to DTO
		List<RentACarCompanyDTO> ret = new ArrayList<>();
		for (RentACarCompany company : companies) {
			ret.add(new RentACarCompanyDTO(company));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/getFastCars/{companyId}", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<CarDTO>> getFastCars(@PathVariable Integer companyId) throws ResourceNotFoundException {

		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}

		Iterable<Car> cars = carService.findAllFast(companyId);

		// convert companies to DTO
		List<CarDTO> ret = new ArrayList<>();
		for (Car c : cars) {
			ret.add(new CarDTO(c));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/getFastCars/{city}/{date}", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<CarDTO>> getFastCars(@PathVariable String city, @PathVariable String date)
			throws ResourceNotFoundException, ParseException {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf1.parse(date);

		Iterable<RentACarCompany> companies = rentACarCompanyService.findAll();

		// convert companies to DTO
		List<CarDTO> ret = new ArrayList<>();
		for (RentACarCompany company : companies) {

			boolean has = false;
			for (BranchOffice bo : company.getBranchOffices()) {
				if (bo.getLocation().getCity().equals(city)) {
					has = true;
					break;
				}
			}
			if (has) {
				for (Car c : company.getCars()) {
					if (c.getIsFast() && c.getBeginDate().compareTo(start) == 0) {
						CarDTO carDto = new CarDTO(c);
						carDto.setRentACarCompany(new RentACarCompanyDTO(c.getRentACarCompany()));
						ret.add(carDto);
						
					}
				}
			}
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/addCarToFastReservation/{companyId}/{carId}/{discount}/{beginDate}/{endDate}/{puBoId}/{doBoId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> addCarToFastReservation(@PathVariable Integer companyId, @PathVariable Integer carId,
			@PathVariable Double discount, @PathVariable String beginDate, @PathVariable String endDate, @PathVariable Integer puBoId, @PathVariable Integer doBoId)
			throws ResourceNotFoundException, ParseException {

		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found.");
		}

		Optional<Car> car = carCompany.get().getCars().stream().filter(o -> o.getId().equals(carId)).findFirst();
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car not found in that rent a car company.");
		}
		
		Optional<BranchOffice> boPu = carCompany.get().getBranchOffices().stream().filter(o -> o.getId().equals(puBoId)).findFirst();
		if (!boPu.isPresent()) {
			throw new ResourceNotFoundException(puBoId.toString(), "Pick up branch office is not found in that rent a car company.");
		}
		
		Optional<BranchOffice> boDo = carCompany.get().getBranchOffices().stream().filter(o -> o.getId().equals(doBoId)).findFirst();

		if (!boDo.isPresent()) {
			throw new ResourceNotFoundException(doBoId.toString(), "Drop off branch office is not found in that rent a car company.");
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

		Date start = sdf1.parse(beginDate);
		Date end = sdf1.parse(endDate);

		car.get().setIsFast(true);
		if (discount > car.get().getPrice()) {
			car.get().setDiscount(car.get().getPrice());
		} else {
			car.get().setDiscount(discount);
		}
		car.get().setBeginDate(start);
		car.get().setEndDate(end);

		car.get().setFastPickUpBranchOffice(boPu.get().getId());
		car.get().setFastDropOffBranchOffice(boDo.get().getId());
		
		Car c = carService.saveCar(car.get());
		return new ResponseEntity<>(c, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value = "/removeCarFromFastReservation/{companyId}/{carId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> removeCarFromFastReservation(@PathVariable Integer companyId, @PathVariable Integer carId)
			throws ResourceNotFoundException, ParseException {

		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found.");
		}

		Optional<Car> car = carCompany.get().getCars().stream().filter(o -> o.getId().equals(carId)).findFirst();
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car not found in that rent a car company.");
		}

		car.get().setIsFast(false);

		Car c = carService.saveCar(car.get());
		return new ResponseEntity<>(c, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/cancelCarReservation/{reservationId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> cancelCarReservation(@PathVariable Integer reservationId, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		
		Optional<CarReservation> carReservation = carReservationService.findCarReservation(reservationId);
		
		if(!carReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Car reservation not found.");
		}		
		
		Customer c = carReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}
		
		Date today = new Date();
		long diff = carReservation.get().getPickUpDate().getTime() - today.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
		if(days < 2 ) {
			throw new RequestDataException("You can't cancel your reservation 3 days before pick up day.");
		}
		
		carReservation.get().setActive(false);
		
		CarReservation cr = carReservationService.saveCarReservation(carReservation.get());
		System.err.println("Rezervcija automobila je otkazanaaaa");
		return new ResponseEntity<>(cr.getId(), HttpStatus.OK);
	}
}
