package isa.project.controller.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.UserDTO;
import isa.project.model.users.AvioCompanyAdmin;
import isa.project.model.users.HotelAdmin;
import isa.project.model.users.RentACarAdmin;
import isa.project.service.hotel.HotelService;
import isa.project.service.users.AuthorityService;
import isa.project.service.users.AvioCompanyAdminService;
import isa.project.service.users.HotelAdminService;
import isa.project.service.users.RentACarAdminService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="sys")
public class SystemAdminController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	HotelAdminService hotelAdminService;
	
	@Autowired
	AvioCompanyAdminService avioCompanyService;
	
	@Autowired
	RentACarAdminService rentACarCompanyService;
	
	@Autowired 
	HotelService hotelService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value="/hotelAdmin/{hotelId}", method = RequestMethod.POST)
	public ResponseEntity<?> createHotelAdmin(@PathVariable Integer hotelId, @RequestBody UserDTO userDTO){
		HotelAdmin hotelAdmin = new HotelAdmin(userDTO.getUsername(), 
				userDTO.getPassword(),
				userDTO.getFirstName(),
				userDTO.getLastName(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber(),
				userDTO.getAddress());
		
		
		hotelAdmin.addAuthority(authorityService.findByName("HOTELADMIN").get());
		hotelAdmin.setHotel(hotelService.findHotel(hotelId).get()); // TODO: Moze da ne postoji hotel, treba uraditi validaciju
		System.err.println("REGISTRACIJA hotel admina: " + hotelAdmin.getUsername() + " PASSWORD: " + hotelAdmin.getPassword());
		
		
        try {
        	hotelAdminService.registerHotelAdmin(hotelAdmin);
        } catch (DataIntegrityViolationException e) {

            logger.warn("Integrity constraint violated");
            return ResponseEntity.status(409).body(hotelAdmin);
        }
		
        return ResponseEntity.ok(hotelAdmin);
	}
	
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value="/avioCompanyAdmin/{avioCompanyId}", method = RequestMethod.POST)
	public ResponseEntity<?> createAvioCompanyAdmin(@PathVariable Integer avioCompanyId, @RequestBody UserDTO userDTO){
		AvioCompanyAdmin avioCompanyAdmin = new AvioCompanyAdmin(userDTO.getUsername(), 
				userDTO.getPassword(),
				userDTO.getFirstName(),
				userDTO.getLastName(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber(),
				userDTO.getAddress());
		
		avioCompanyAdmin.addAuthority(authorityService.findByName("AVIOADMIN").get());
		System.err.println("REGISTRACIJA avio admina: " + avioCompanyAdmin.getUsername() + " PASSWORD: " + avioCompanyAdmin.getPassword());
		
        try {
        	avioCompanyService.registerAvioCompanyAdmin(avioCompanyAdmin);
        } catch (DataIntegrityViolationException e) {

            logger.warn("Integrity constraint violated");
            return ResponseEntity.status(409).body(avioCompanyAdmin);
        }
		
        return ResponseEntity.ok(avioCompanyAdmin);
	}
	
	
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value="/rentACarCompanyAdmin/{rentACarCompanyId}", method = RequestMethod.POST)
	public ResponseEntity<?> createRentACarCompanyAdmin(@PathVariable Integer rentACarCompanyId, @RequestBody UserDTO userDTO){
		RentACarAdmin rentACarCompanyAdmin = new RentACarAdmin(userDTO.getUsername(), 
				userDTO.getPassword(),
				userDTO.getFirstName(),
				userDTO.getLastName(),
				userDTO.getEmail(),
				userDTO.getPhoneNumber(),
				userDTO.getAddress());
		
		rentACarCompanyAdmin.addAuthority(authorityService.findByName("CARADMIN").get());
		System.err.println("REGISTRACIJA car admina: " + rentACarCompanyAdmin.getUsername() + " PASSWORD: " + rentACarCompanyAdmin.getPassword());
		
        try {
        	rentACarCompanyService.registerRentACarCompanyAdmin(rentACarCompanyAdmin);
        } catch (DataIntegrityViolationException e) {

            logger.warn("Integrity constraint violated");
            return ResponseEntity.status(409).body(rentACarCompanyAdmin);
        }
		
        return ResponseEntity.ok(rentACarCompanyAdmin);
	}
	
	
}
