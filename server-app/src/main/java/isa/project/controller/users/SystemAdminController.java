package isa.project.controller.users;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.UserDTO;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.hotel.Hotel;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.users.AirCompanyAdmin;
import isa.project.model.users.HotelAdmin;
import isa.project.model.users.RentACarAdmin;
import isa.project.model.users.SystemAdmin;
import isa.project.service.aircompany.AirCompanyService;
import isa.project.service.hotel.HotelService;
import isa.project.service.rentacar.RentACarCompanyService;
import isa.project.service.users.AirCompanyAdminService;
import isa.project.service.users.AuthorityService;
import isa.project.service.users.HotelAdminService;
import isa.project.service.users.RentACarAdminService;
import isa.project.service.users.SystemAdminService;

@RestController
@RequestMapping(value = "sys")
public class SystemAdminController {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private HotelAdminService hotelAdminService;

	@Autowired
	private AirCompanyAdminService airCompanyAdminService;

	@Autowired
	private RentACarAdminService rentACarCompanyAdminService;
	
	@Autowired
	private SystemAdminService sysService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private AirCompanyService airCompanyService;

	@Autowired
	private RentACarCompanyService rentACarCompanyService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Register new administrator for hotel.
	 * 
	 * @param hotelId id of hotel that administrator is assigned for.
	 * @param userDTO information about administrator such as email, name, etc.
	 * @return
	 */
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/hotelAdmin/{hotelId}", method = RequestMethod.POST)
	public ResponseEntity<?> createHotelAdmin(@PathVariable Integer hotelId, @Valid @RequestBody UserDTO userDTO) {
		
		// create new hotel administrator
		HotelAdmin hotelAdmin = new HotelAdmin(userDTO);
		hotelAdmin.addAuthority(authorityService.findByName("HOTELADMIN").get());

		// set hotel
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		if (hotel.isPresent()) {
			hotelAdmin.setHotel(hotel.get());
		} else {
			return new ResponseEntity<>("Hotel does not exist.", HttpStatus.BAD_REQUEST);
		}

		// save hotel admin
		try {
			hotelAdminService.registerHotelAdmin(hotelAdmin);
		} catch (DataIntegrityViolationException e) {
			// email or phone number are not unique
			logger.warn("Integrity constraint violated");
			return ResponseEntity.status(409).body(hotelAdmin);
		}

		return ResponseEntity.ok(hotelAdmin);
	}

	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/airCompanyAdmin/{airCompanyId}", method = RequestMethod.POST)
	public ResponseEntity<?> createAvioCompanyAdmin(@PathVariable Integer airCompanyId,
			@Valid @RequestBody UserDTO userDTO) {
		// create new air company administrator
		AirCompanyAdmin airCompanyAdmin = new AirCompanyAdmin(userDTO);
		airCompanyAdmin.addAuthority(authorityService.findByName("AIRADMIN").get());

		// set air company
		Optional<AirCompany> airCompany = airCompanyService.findAircompany(airCompanyId);
		if (airCompany.isPresent()) {
			airCompanyAdmin.setAirCompany(airCompany.get());
		} else {
			return new ResponseEntity<>("Air company does not exist.", HttpStatus.BAD_REQUEST);
		}

		try {
			airCompanyAdminService.registerAvioCompanyAdmin(airCompanyAdmin);
		} catch (DataIntegrityViolationException e) {
			// email or phone number are not unique
			logger.warn("Integrity constraint violated");
			return ResponseEntity.status(409).body(airCompanyAdmin);
		}

		return ResponseEntity.ok(airCompanyAdmin);
	}

	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/rentACarCompanyAdmin/{rentACarCompanyId}", method = RequestMethod.POST)
	public ResponseEntity<?> createRentACarCompanyAdmin(@PathVariable Integer rentACarCompanyId,
			@Valid @RequestBody UserDTO userDTO) {
		// create admin
		RentACarAdmin rentACarCompanyAdmin = new RentACarAdmin(userDTO);
		rentACarCompanyAdmin.addAuthority(authorityService.findByName("CARADMIN").get());

		// set rent a car company
		Optional<RentACarCompany> rentACarCompany = rentACarCompanyService.findRentACarCompany(rentACarCompanyId);
		if (rentACarCompany.isPresent()) {
			rentACarCompanyAdmin.setRentACarCompany(rentACarCompany.get());
		} else {
			return new ResponseEntity<>("Rent a car company does not exist.", HttpStatus.BAD_REQUEST);
		}

		try {
			rentACarCompanyAdminService.registerRentACarCompanyAdmin(rentACarCompanyAdmin);
		} catch (DataIntegrityViolationException e) {
			// email or phone number are not unique
			logger.warn("Integrity constraint violated");
			return ResponseEntity.status(409).body(rentACarCompanyAdmin);
		}

		return ResponseEntity.ok(rentACarCompanyAdmin);
	}
	
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/addSystemAdmin", method = RequestMethod.POST)
	public ResponseEntity<?> addSystemAdmin(@Valid @RequestBody UserDTO userDTO) {
		// create admin
		SystemAdmin systemAdmin = new SystemAdmin(userDTO);
		systemAdmin.addAuthority(authorityService.findByName("SYS").get());
	
		try {
			sysService.registerSystemAdmin(systemAdmin);
		} catch (DataIntegrityViolationException e) {
			// email or phone number are not unique
			logger.warn("Integrity constraint violated");
			return ResponseEntity.status(409).body(systemAdmin);
		}

		return ResponseEntity.ok(systemAdmin);
	}

}
