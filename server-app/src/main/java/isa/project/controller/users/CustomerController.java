package isa.project.controller.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import isa.project.dto.reservations.ReservationDTO;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.dto.users.UserDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.Customer;
import isa.project.model.users.Reservation;
import isa.project.model.users.User;
import isa.project.model.users.security.VerificationToken;
import isa.project.security.TokenUtils;
import isa.project.security.auth.JwtAuthenticationRequest;
import isa.project.service.EmailService;
import isa.project.service.VerificationTokenService;
import isa.project.service.users.AuthorityService;
import isa.project.service.users.CustomUserDetailsService;
import isa.project.service.users.CustomerService;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private VerificationTokenService tokenService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private EmailService emailService;

	/**
	 * Register new customer. Saves customer to database and send confirmation
	 * email. Returns error response if required customer data is missing or email
	 * or phone number are not unique.
	 * 
	 * @param customerDTO data for customer being registered.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO customerDTO, WebRequest request) {
		// Create new customer
		Customer customer = new Customer(customerDTO);
		customer.addAuthority(authorityService.findByName("CUSTOMER").get());

		// save new customer to database
		try {
			customerService.registerCustomer(customer);
		} catch (DataIntegrityViolationException e) {
			// email or phone number are not unique
			return ResponseEntity.status(409).body(customer);
		}

		// send verification email to customer
		try {
			confirmRegistration(customer);
		} catch (Exception e) {
			// TODO: Da li treba izbrisati korisnika iz baze?
			return new ResponseEntity<>("Confirmation email could not be sent. Your email migth not be valid.",
					HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(customer);
	}

	/**
	 * Confirms email of registered user.
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/confirmRegistration", method = RequestMethod.GET)
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token, HttpServletResponse httpServletResponse) {
		Optional<VerificationToken> verificationToken = tokenService.findByToken(token);

		if (!verificationToken.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.LOCATION, "http://localhost:4200").build();
		}

		Customer customer = (Customer) verificationToken.get().getUser();
		customer.setConfirmedMail(true);
		customerService.saveCustomer(customer);
		// redirekcija gde zelimo
		return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "http://localhost:4200").build();
	}

	/**
	 * Tries to login user.
	 * 
	 * @param authenticationRequest contains email and password
	 * @param response
	 * @param device                type of device on which user wants to login
	 * @return
	 * @throws AuthenticationException
	 * @throws IOException
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			@Valid @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response,
			Device device) throws AuthenticationException, IOException {

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
						authenticationRequest.getPassword()));

		// Ubaci username + password u kontext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Kreiraj token
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		String token = this.tokenUtils.generateToken(userDetails, device);

		// Pronadji korisnika (potrebno da bi se znalo mora li se sifra menjati)
		User user = this.customUserDetailsService.loadByEmail(userDetails.getUsername()).get();

		// Vrati token kao odgovor na uspesno autentifikaciju
		return ResponseEntity.ok(new AuthenticationResponse(token, user.getNeedsPasswordChange()));
	}

	/**
	 * Creates email confirmation message and send it to user.
	 * 
	 * @param customer to whom message is being sent
	 * @throws MessagingException
	 * @throws InterruptedException
	 * @throws MailException
	 */
	private void confirmRegistration(Customer customer) throws MessagingException, MailException, InterruptedException {
		// Token
		String token = UUID.randomUUID().toString();

		// Cuvanje tokena za customera
		tokenService.createVerificationToken(customer, token);

		String recipientMail = customer.getEmail();
		String subject = "Potvrda registracije";
		String confirmationUrl = "http://localhost:8080/customers/confirmRegistration?token=" + token;
		String message = "<html><body>Click here to activate your account<br>" + confirmationUrl + "</body></html>";
		emailService.sendNotificaitionAsync(recipientMail, subject, message);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/getAllReservations", method = RequestMethod.GET)
	public ResponseEntity<?> getAllReservations(HttpServletRequest request) throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));

		Optional<Customer> customer = customerService.findCustomer(email);

		if (!customer.isPresent()) {
			throw new ResourceNotFoundException(email, "Customer is not found");
		}

		List<ReservationDTO> ret = new ArrayList<ReservationDTO>();
		if (customer.get().getReservations() != null) {
			for (Reservation r : customer.get().getReservations()) {
				ret.add(new ReservationDTO(r));
			}
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

}
