package isa.project.controller.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import isa.project.dto.users.CustomerDTO;
import isa.project.dto.users.UserDTO;
import isa.project.event.OnRegistrationCompleteEvent;
import isa.project.model.VerificationToken;
import isa.project.model.json.AuthenticationResponse;
import isa.project.model.users.Authority;
import isa.project.model.users.Customer;
import isa.project.security.TokenUtils;
import isa.project.security.auth.JwtAuthenticationRequest;
import isa.project.service.VerificationTokenService;
import isa.project.service.users.AuthorityService;
import isa.project.service.users.CustomUserDetailsService;
import isa.project.service.users.CustomerService;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping(value="customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	TokenUtils tokenUtils;
	
	@Autowired 
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private VerificationTokenService tokenService;
	
	@Autowired
	private AuthorityService authorityService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Returns DTO objects for customers. Objects contain username, password, first name, last name, email, phone number, address and information about confirmation mail.
	 * @return information about all customers.
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value="/secured/all", method = RequestMethod.GET)
	public ResponseEntity<List<UserDTO>> getAllCustomers(){
		System.out.println("SECUREDs");
		Iterable<Customer> customers = customerService.findAll();
		
		//convert companies to DTO
		List<UserDTO> ret = new ArrayList<>();
		for(Customer customer: customers) {
			ret.add(new UserDTO(customer));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/loginPage")
	public String hello()
	{
		return "login";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody CustomerDTO customerDTO, WebRequest request){

		Customer customer = new Customer(customerDTO.getUsername(), 
				customerDTO.getPassword(),
				customerDTO.getFirstName(),
				customerDTO.getLastName(),
				customerDTO.getEmail(),
				customerDTO.getPhoneNumber(),
				customerDTO.getAddress());
				
		Optional<Authority> authority = authorityService.findByName("CUSTOMER");
		if( !authority.isPresent() ) {
			authorityService.saveAuthority(new Authority("CUSTOMER"));
		}
		
		customer.addAuthority(authorityService.findByName("CUSTOMER").get());
		System.out.println("REGISTRACIJA: " + customer.getUsername() + " PASSWORD: " + customer.getPassword());
		
        try {
        	customerService.registerCustomer(customer);
        } catch (DataIntegrityViolationException e) {

            logger.warn("Integrity constraint violated");
            return ResponseEntity.status(409).body(customer);
        }

        try {
            String appUrl = request.getContextPath() + "/customers";
            logger.warn("Nzm sta je ovo "  + appUrl);
            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(customer, appUrl));

        } catch(Exception e) {
        	logger.info(e.getMessage());
        }

        return ResponseEntity.ok(customer);
    }
	
	@RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = tokenService.findByToken(token);

        if(verificationToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.LOCATION, "/api/").build();
        }

        Customer customer = verificationToken.getCustomer();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.LOCATION, "/api/").build();
        }

        customer.setConfirmedMail(true);
        customerService.saveCustomer(customer);

        // redirekcija gde zelimo
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/api/").build();
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response, Device device) throws AuthenticationException, IOException {
		System.out.println("USAO U LOGIN!!!");
		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		// Ubaci username + password u kontext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = this.tokenUtils.generateToken(userDetails, device);
		
		// Vrati token kao odgovor na uspesno autentifikaciju
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
}
