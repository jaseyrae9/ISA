package isa.project.controller;

import static isa.project.constants.RentACarCompanyConstants.AVERAGE_RATING;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_COUNT;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_DESCRIPTION;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_NAME;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static isa.project.constants.RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_NAME;
import static isa.project.constants.RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_DESCRIPTION;
import java.nio.charset.Charset;

import static isa.project.constants.RentACarCompanyConstants.LOCATION_ADDRESS;
import static isa.project.constants.RentACarCompanyConstants.LOCATION_CITY;
import static isa.project.constants.RentACarCompanyConstants.LOCATION_COUNTRY;
import static isa.project.constants.RentACarCompanyConstants.LOCATION_LON;
import static isa.project.constants.RentACarCompanyConstants.LOCATION_LAT;
import static isa.project.constants.RentACarCompanyConstants.SYS_ADMIN_EMAIL;
import static isa.project.constants.RentACarCompanyConstants.SYS_ADMIN_PASSWORD;
import static isa.project.constants.RentACarCompanyConstants.CAR_ADMIN_EMAIL;
import static isa.project.constants.RentACarCompanyConstants.CAR_ADMIN_PASSWORD;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import isa.project.TestUtil;
import isa.project.constants.RentACarCompanyConstants;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.shared.Location;
import isa.project.security.auth.JwtAuthenticationRequest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.web.FilterChainProxy;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RentACarCompanyControllerTest {

	private static final String URL_PREFIX = "/rent_a_car_companies";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private String accessTokenSysAdmin;

	private String accessTokenCarAdmin;

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

		ResponseEntity<AuthenticationResponse> responseEntityRent = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(CAR_ADMIN_EMAIL, CAR_ADMIN_PASSWORD), AuthenticationResponse.class);
		accessTokenCarAdmin = responseEntityRent.getBody().getToken();
	}

	@Test
	public void testGetAllCompanies() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/allCompanies")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(RENT_A_CAR_COMPANY_COUNT)))
				.andExpect(
						jsonPath("$.[*].id").value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(RENT_A_CAR_COMPANY_NAME)))
				.andExpect(jsonPath("$.[*].averageRating").value(hasItem(AVERAGE_RATING)))
				.andExpect(jsonPath("$.[*].description").value(hasItem(RENT_A_CAR_COMPANY_DESCRIPTION)))
				.andExpect(jsonPath("$.[*].location.id")
						.value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue())));
	}

	@Test
	public void testGetAllCompaniesPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/all?page=0&size=2")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content.[*].id")
						.value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue())))
				.andExpect(jsonPath("$.content.[*].name").value(hasItem(RENT_A_CAR_COMPANY_NAME)))
				.andExpect(jsonPath("$.content.[*].averageRating").value(hasItem(AVERAGE_RATING)))
				.andExpect(jsonPath("$.content.[*].description").value(hasItem(RENT_A_CAR_COMPANY_DESCRIPTION)))
				.andExpect(jsonPath("$.content.[*].location.id")
						.value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue())));
	}

	// get/{id}
	@Test
	public void testGetCompany() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/get/" + RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.name").value(RENT_A_CAR_COMPANY_NAME))
				.andExpect(jsonPath("$.averageRating").value(AVERAGE_RATING))
				.andExpect(jsonPath("$.description").value(RENT_A_CAR_COMPANY_DESCRIPTION))
				.andExpect(jsonPath("$.location.id")
						.value(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue()));
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddRentACarCompany() throws Exception {
		RentACarCompany company = new RentACarCompany();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		company.setName(NEW_RENT_A_CAR_COMPANY_NAME);
		company.setDescription(NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		company.setLocation(location);
		String json = TestUtil.json(company);
		this.mockMvc.perform(post(URL_PREFIX + "/add").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testEditRentACarCompany() throws Exception {
		RentACarCompany company = new RentACarCompany();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		company.setId(RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue());
		company.setName(NEW_RENT_A_CAR_COMPANY_NAME);
		company.setDescription(NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		company.setLocation(location);
		String json = TestUtil.json(company);
		this.mockMvc.perform(put(URL_PREFIX + "/edit/" + RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue()).header("Authorization", "Bearer " + accessTokenCarAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}	
	
}
