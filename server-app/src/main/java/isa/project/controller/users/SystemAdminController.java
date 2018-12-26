//package isa.project.controller.users;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import isa.project.dto.hotel.HotelDTO;
//import isa.project.dto.users.UserDTO;
//import isa.project.model.hotel.Hotel;
//import isa.project.model.users.Authority;
//import isa.project.model.users.Customer;
//import isa.project.model.users.HotelAdmin;
//
//@RestController
//@CrossOrigin(origins="*")
//@RequestMapping(value="sys")
//public class SystemAdminController {
//	
//	@PreAuthorize("hasAnyRole('SYS')")
//	@RequestMapping(value="/hotelAdmin/{hotelId}", method = RequestMethod.POST)
//	public ResponseEntity<?> createHotelAdmin(@PathVariable Integer hotelId, @RequestBody UserDTO user){
//		HotelAdmin hotelAdmin = new HotelAdmin(customerDTO.getUsername(), 
//				customerDTO.getPassword(),
//				customerDTO.getFirstName(),
//				customerDTO.getLastName(),
//				customerDTO.getEmail(),
//				customerDTO.getPhoneNumber(),
//				customerDTO.getAddress());
//				
//		Optional<Authority> authority = authorityService.findByName("CUSTOMER");
//		if( !authority.isPresent() ) {
//			authorityService.saveAuthority(new Authority("CUSTOMER"));
//		}
//		
//		customer.addAuthority(authorityService.findByName("CUSTOMER").get());
//		System.out.println("REGISTRACIJA: " + customer.getUsername() + " PASSWORD: " + customer.getPassword());
//		
//        try {
//        	customerService.registerCustomer(customer);
//        } catch (DataIntegrityViolationException e) {
//
//            logger.warn("Integrity constraint violated");
//            return ResponseEntity.status(409).body(customer);
//        }
//		
//		return new ResponseEntity<>()
//	}
//
//	public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO){
//		Hotel hotel = new Hotel(hotelDTO.getName(), hotelDTO.getDescription());
//		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(hotel)), HttpStatus.CREATED);	
//	}
//	
//	@PreAuthorize("hasAnyRole('SYS')")
//	@RequestMapping(value="/hotelAdmin", method = RequestMethod.POST)
//	public ResponseEntity<?> createAirAdmin(){
//		
//		return new ResponseEntity<>(ret, HttpStatus.OK);
//	}
//	
//	@PreAuthorize("hasAnyRole('SYS')")
//	@RequestMapping(value="/hotelAdmin", method = RequestMethod.POST)
//	public ResponseEntity<?> createREntAdmin(){
//		
//		return new ResponseEntity<>(ret, HttpStatus.OK);
//	}
//}
