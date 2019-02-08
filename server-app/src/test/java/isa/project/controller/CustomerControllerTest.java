package isa.project.controller;

import static isa.project.constants.CustomerConstants.CUSTOMER_EMAIL;
import static isa.project.constants.CustomerConstants.CUST_EMAIL;
import static isa.project.constants.CustomerConstants.CUSTOMER_PASSWORD;
import static isa.project.constants.CustomerConstants.CUST_PASS;
import static isa.project.constants.CustomerConstants.FIRST_NAME;
import static isa.project.constants.CustomerConstants.LAST_NAME;
import static isa.project.constants.CustomerConstants.ADDRESS;
import static isa.project.constants.CustomerConstants.PHONE_NUMBER;
import static isa.project.constants.CustomerConstants.ADMIN_EMAIL;
import static isa.project.constants.CustomerConstants.ADMIN_PASSWORD;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import isa.project.TestUtil;
import isa.project.constants.CustomerConstants;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.dto.users.UserDTO;
import isa.project.model.users.Customer;
import isa.project.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

	private static final String URL_PREFIX = "/customers";

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private String accessTokenCustomer;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.build();

		ResponseEntity<AuthenticationResponse> responseEntityCustomer = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(CUST_EMAIL, CUST_PASS), AuthenticationResponse.class);
		accessTokenCustomer = responseEntityCustomer.getBody().getToken();
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testRegister() throws Exception {
		UserDTO user = new UserDTO();
		user.setEmail(CUSTOMER_EMAIL);
		user.setPassword(CUSTOMER_PASSWORD);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPhoneNumber(PHONE_NUMBER);
		user.setAddress(ADDRESS);
		Customer customer = new Customer(user);

		String json = TestUtil.json(customer);
		this.mockMvc.perform(post(URL_PREFIX + "/register").contentType(contentType).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void testLogin() throws Exception {
		JwtAuthenticationRequest request = new JwtAuthenticationRequest(ADMIN_EMAIL, ADMIN_PASSWORD);

		String json = TestUtil.json(request);
		this.mockMvc.perform(post(URL_PREFIX + "/login").contentType(contentType).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllReservations() throws Exception {
		this.mockMvc
				.perform(get(URL_PREFIX + "/getAllReservations").header("Authorization",
						"Bearer " + accessTokenCustomer))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(CustomerConstants.RESERVATION_ID.intValue())))
				.andExpect(jsonPath("$.[*].carReservation.id")
						.value(hasItem(CustomerConstants.CAR_RESERVATION.intValue())))
				.andExpect(jsonPath("$.[*].roomReservation.id").doesNotExist())
				.andExpect(jsonPath("$.[*].flightReservation.id").doesNotExist())
				.andExpect(jsonPath("$.[*].discount").value(hasItem(CustomerConstants.DISCOUNT)));
	}
	
	
}
