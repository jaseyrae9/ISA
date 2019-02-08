package isa.project.controller;

import static isa.project.constants.CustomerConstants.CUST_EMAIL;
import static isa.project.constants.CustomerConstants.CUST_PASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

import isa.project.dto.users.AuthenticationResponse;
import isa.project.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RateControllerTest {

	private static final String URL_PREFIX = "/reservation";
	
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
	public void testRateCarCompany() throws Exception {	
		this.mockMvc.perform(put(URL_PREFIX + "/rateCarCompany/666/666/4").header("Authorization", "Bearer " + accessTokenCustomer))
		.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); 

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testRateCar() throws Exception {
		this.mockMvc.perform(put(URL_PREFIX + "/rateCar/1001/100/4").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); // car doesn't exist	
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRateAirCompany() throws Exception {
		this.mockMvc.perform(put(URL_PREFIX + "/rateAirCompany/165/100/5").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); // air company doesn't exist	
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRateFlight() throws Exception {
		this.mockMvc.perform(put(URL_PREFIX + "/rateFlight/1001/1001/4").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); // flight and reservation doesn't exist	
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRateRoom() throws Exception {
		this.mockMvc.perform(put(URL_PREFIX + "/rateRoom/600/100/4").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); // single room reservation doesn't exist	
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testRateHotel() throws Exception {
		this.mockMvc.perform(put(URL_PREFIX + "/rateHotel/1001/100/4").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(content().contentType(contentType)).andExpect(status().isNotFound()); 	
	}
	
}
