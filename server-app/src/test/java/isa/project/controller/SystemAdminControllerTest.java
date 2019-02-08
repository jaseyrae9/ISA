package isa.project.controller;

import static isa.project.constants.RentACarCompanyConstants.SYS_ADMIN_EMAIL;
import static isa.project.constants.RentACarCompanyConstants.SYS_ADMIN_PASSWORD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

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
import isa.project.dto.users.AuthenticationResponse;
import isa.project.dto.users.UserDTO;
import isa.project.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SystemAdminControllerTest {

	private static final String URL_PREFIX = "/sys";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private String accessTokenSysAdmin;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@PostConstruct
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.build();

		ResponseEntity<AuthenticationResponse> responseEntitySys = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(SYS_ADMIN_EMAIL, SYS_ADMIN_PASSWORD), AuthenticationResponse.class);
		accessTokenSysAdmin = responseEntitySys.getBody().getToken();
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddHotelAdmin() throws Exception {
		UserDTO user = new UserDTO();
	    user.setEmail("jelena.surlan9@gmail.com");
	    user.setPassword("sifra");
	    user.setFirstName("Jelena");
	    user.setLastName("Surlan");
	    user.setPhoneNumber("0611855596");
	    user.setAddress("adresa");
	    user.setConfirmedMail(true);
	    String json = TestUtil.json(user);
	    this.mockMvc.perform(post(URL_PREFIX +"/hotelAdmin/100").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddCarAdmin() throws Exception {
		UserDTO user = new UserDTO();
	    user.setEmail("jelena.surlan9@gmail.com");
	    user.setPassword("sifra");
	    user.setFirstName("Jelena");
	    user.setLastName("Surlan");
	    user.setPhoneNumber("0611855596");
	    user.setAddress("adresa");
	    user.setConfirmedMail(true);
	    String json = TestUtil.json(user);
	    this.mockMvc.perform(post(URL_PREFIX +"/rentACarCompanyAdmin/100").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddAirAdmin() throws Exception {
		UserDTO user = new UserDTO();
	    user.setEmail("jelena.surlan9@gmail.com");
	    user.setPassword("sifra");
	    user.setFirstName("Jelena");
	    user.setLastName("Surlan");
	    user.setPhoneNumber("0611855596");
	    user.setAddress("adresa");
	    user.setConfirmedMail(true);
	    String json = TestUtil.json(user);
	    this.mockMvc.perform(post(URL_PREFIX +"/airCompanyAdmin/100").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddSystemAdmin() throws Exception {
		UserDTO user = new UserDTO();
	    user.setEmail("jelena.surlan9@gmail.com");
	    user.setPassword("sifra");
	    user.setFirstName("Jelena");
	    user.setLastName("Surlan");
	    user.setPhoneNumber("0611855596");
	    user.setAddress("adresa");
	    user.setConfirmedMail(true);
		
		String json = TestUtil.json(user);
		this.mockMvc.perform(post(URL_PREFIX +"/addSystemAdmin").header("Authorization", "Bearer " + accessTokenSysAdmin)
					.contentType(contentType).content(json)).andExpect(status().isOk());
	}
}
