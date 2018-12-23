package isa.project.controller.users;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.common.DeviceProvider;
import isa.project.dto.users.RegisterCustomerDTO;
import isa.project.model.UserTokenState;
import isa.project.model.users.Authority;
import isa.project.model.users.Customer;
import isa.project.model.users.User;
import isa.project.security.TokenUtils;
import isa.project.security.auth.JwtAuthenticationRequest;
import isa.project.service.users.AuthorityService;
import isa.project.service.users.CustomUserDetailsService;
import isa.project.service.users.CustomerService;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	protected final Log LOGGER = LogFactory.getLog(getClass());

	@Autowired
	TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private DeviceProvider deviceProvider;

	@PostMapping("/register")
	public ResponseEntity<RegisterCustomerDTO> register(@Valid  @RequestBody RegisterCustomerDTO user) {
		Customer customer = new Customer(user.getUsername(), user.getPassword(), user.getFirstName(),
				user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
			
		
		Optional<Authority> authority = authorityService.findByName("CUSTOMER");
		if( !authority.isPresent() ) {
			authorityService.saveAuthority(new Authority("CUSTOMER"));
		}
		
		customer.addAuthority(authorityService.findByName("CUSTOMER").get());
		
		return new ResponseEntity<>(new RegisterCustomerDTO(customerService.saveCustomer(customer)), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
			HttpServletResponse response, Device device) throws AuthenticationException, IOException {

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));

		// Ubaci username + password u kontext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token
		User user = (User) authentication.getPrincipal();
		String jwt = tokenUtils.generateToken(user.getUsername(), device);
		int expiresIn = tokenUtils.getExpiredIn(device);

		// Vrati token kao odgovor na uspesno autentifikaciju
		return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
	}
}