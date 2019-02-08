package isa.project.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static isa.project.constants.AirCompanyConstants.AIR_COMPANY_DESCRIPTION;
import static isa.project.constants.AirCompanyConstants.AIR_COMPANY_NAME;
import static isa.project.constants.AirCompanyConstants.AIR_COMPANY_COUNT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import isa.project.constants.AirCompanyConstants;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import isa.project.security.auth.JwtAuthenticationRequest;
import static isa.project.constants.AirCompanyConstants.LOCATION_ADDRESS;
import static isa.project.constants.AirCompanyConstants.LOCATION_CITY;
import static isa.project.constants.AirCompanyConstants.LOCATION_COUNTRY;
import static isa.project.constants.AirCompanyConstants.LOCATION_LON;
import static isa.project.constants.AirCompanyConstants.LOCATION_LAT;
import static isa.project.constants.AirCompanyConstants.SYS_ADMIN_EMAIL;
import static isa.project.constants.AirCompanyConstants.SYS_ADMIN_PASSWORD;
import static isa.project.constants.AirCompanyConstants.NEW_AIR_COMPANY_NAME;
import static isa.project.constants.AirCompanyConstants.NEW_AIR_DESCRIPTION;
import static isa.project.constants.AirCompanyConstants.AIR_ADMIN_EMAIL;
import static isa.project.constants.AirCompanyConstants.AIR_ADMIN_PASSWORD;
import static isa.project.constants.AirCompanyConstants.AIR_COMPANY_ID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AirCompanyControllerTest {

	private static final String URL_PREFIX = "/aircompanies";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private String accessTokenSysAdmin;

	private String accessTokenAirAdmin;

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

		ResponseEntity<AuthenticationResponse> responseEntityAir = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(AIR_ADMIN_EMAIL, AIR_ADMIN_PASSWORD), AuthenticationResponse.class);
		accessTokenAirAdmin = responseEntityAir.getBody().getToken();

	}

	@Test
	public void testGetAllCompanies() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/all")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(AIR_COMPANY_COUNT)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(AirCompanyConstants.AIR_COMPANY_ID.intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(AIR_COMPANY_NAME)))
				.andExpect(jsonPath("$.[*].description").value(hasItem(AIR_COMPANY_DESCRIPTION)))
				.andExpect(jsonPath("$.[*].location.id")
						.value(hasItem(AirCompanyConstants.AIR_COMPANY_LOCATION_ID.intValue())));
	}

	@Test
	public void testGetAllCompaniesPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/allAirCompanies?page=0&size=2")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content.[*].id").value(hasItem(AirCompanyConstants.AIR_COMPANY_ID.intValue())))
				.andExpect(jsonPath("$.content.[*].name").value(hasItem(AIR_COMPANY_NAME)))
				.andExpect(jsonPath("$.content.[*].description").value(hasItem(AIR_COMPANY_DESCRIPTION)))
				.andExpect(jsonPath("$.content.[*].location.id")
						.value(hasItem(AirCompanyConstants.AIR_COMPANY_LOCATION_ID.intValue())));
	}

	@Test
	public void testGetCompany() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/get/" + AirCompanyConstants.AIR_COMPANY_ID)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name").value(AIR_COMPANY_NAME))
				.andExpect(jsonPath("$.description").value(AIR_COMPANY_DESCRIPTION))
				.andExpect(jsonPath("$.location.id").value(AirCompanyConstants.AIR_COMPANY_LOCATION_ID.intValue()));
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddAirCompany() throws Exception {
		AirCompany company = new AirCompany();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		company.setName(NEW_AIR_COMPANY_NAME);
		company.setDescription(NEW_AIR_DESCRIPTION);
		company.setLocation(location);
		String json = TestUtil.json(company);
		this.mockMvc.perform(post(URL_PREFIX + "/add").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(contentType).content(json)).andExpect(status().isCreated());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testEditAirCompany() throws Exception {
		AirCompany company = new AirCompany();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		company.setId(AIR_COMPANY_ID.intValue());
		company.setName(NEW_AIR_COMPANY_NAME);
		company.setDescription(NEW_AIR_DESCRIPTION);
		company.setLocation(location);
		String json = TestUtil.json(company);
		this.mockMvc.perform(put(URL_PREFIX + "/edit/" + AIR_COMPANY_ID.intValue())
				.header("Authorization", "Bearer " + accessTokenAirAdmin).contentType(contentType).content(json))
				.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteDestination() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteDestination/" + AIR_COMPANY_ID.intValue() + "/" + AirCompanyConstants.DESTINATION_ID).header("Authorization", "Bearer " + accessTokenAirAdmin))
		.andExpect(status().isOk()); 
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteDestinationExp() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteDestination/" + 101 + "/" + AirCompanyConstants.DESTINATION_ID).header("Authorization", "Bearer " + accessTokenAirAdmin))
		.andExpect(status().isUnauthorized()); 
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddDestination() throws Exception {		
		Destination destination = new Destination(null, "RUM", "Srbija", "Ruma", "Milosevo dvoriste");
		String json = TestUtil.json(destination);
		this.mockMvc.perform(post(URL_PREFIX + "/addDestination/100").header("Authorization", "Bearer " + accessTokenAirAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddBaggageInformation() throws Exception {		
		AdditionalService as = new AdditionalService("Baggage", "Opis", 2.5);
		String json = TestUtil.json(as);
		this.mockMvc.perform(post(URL_PREFIX + "/addBaggageInformation/100").header("Authorization", "Bearer " + accessTokenAirAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}	

	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteBaggageInformationExp() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteBaggageInformation/" + AIR_COMPANY_ID.intValue() + "/1500").header("Authorization", "Bearer " + accessTokenAirAdmin))
		.andExpect(status().isNotFound()); //baggage doesn't exists
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteBaggageInformationExp2() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteBaggageInformation/" + 102 + "/1500").header("Authorization", "Bearer " + accessTokenAirAdmin))
		.andExpect(status().isUnauthorized()); 
	}
	
	
}
