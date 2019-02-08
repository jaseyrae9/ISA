package isa.project.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import static isa.project.constants.HotelConstants.HOTEL_COUNT;
import static isa.project.constants.HotelConstants.HOTEL_SIZE_COUNT;
import static isa.project.constants.HotelConstants.HOTEL_ADMIN_EMAIL;
import static isa.project.constants.HotelConstants.HOTEL_ADMIN_PASSWORD;
import static isa.project.constants.HotelConstants.SYS_ADMIN_EMAIL;
import static isa.project.constants.HotelConstants.SYS_ADMIN_PASSWORD;
import static isa.project.constants.HotelConstants.LOCATION_ADDRESS;
import static isa.project.constants.HotelConstants.LOCATION_CITY;
import static isa.project.constants.HotelConstants.LOCATION_COUNTRY;
import static isa.project.constants.HotelConstants.LOCATION_LAT;
import static isa.project.constants.HotelConstants.LOCATION_LON;
import static isa.project.constants.HotelConstants.NEW_HOTEL_DESCRIPTION;
import static isa.project.constants.HotelConstants.NEW_HOTEL_NAME;
import static isa.project.constants.HotelConstants.HOTEL_NAME;
import static isa.project.constants.HotelConstants.HOTEL_DESC;
import static isa.project.constants.HotelConstants.AVERAGE_RATING;

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
import isa.project.constants.HotelConstants;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import isa.project.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HotelControllerTest {

	private static final String URL_PREFIX = "/hotels";

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private String accessTokenSysAdmin;

	private String accessTokenHotelAdmin;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private TestRestTemplate restTemplate;

	@PostConstruct
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.build();

		ResponseEntity<AuthenticationResponse> responseEntitySys = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(SYS_ADMIN_EMAIL, SYS_ADMIN_PASSWORD), AuthenticationResponse.class);
		accessTokenSysAdmin = responseEntitySys.getBody().getToken();

		ResponseEntity<AuthenticationResponse> responseEntityHotel = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(HOTEL_ADMIN_EMAIL, HOTEL_ADMIN_PASSWORD), AuthenticationResponse.class);
		accessTokenHotelAdmin = responseEntityHotel.getBody().getToken();

	}

	@Test
	public void testGetAllHotels() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/allHotels")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(HOTEL_COUNT)))
				.andExpect(jsonPath("$.[*].id").value(hasItem(HotelConstants.DB_ID.intValue())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(HOTEL_NAME)))
				.andExpect(jsonPath("$.[*].description").value(hasItem(HOTEL_DESC)))
				.andExpect(jsonPath("$.[*].averageRating").value(hasItem(AVERAGE_RATING)));
	}

	@Test
	public void testGetHotelsPage() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/all?page=0&size=1")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.content", hasSize(HOTEL_SIZE_COUNT)))
				.andExpect(jsonPath("$.content.[*].id").value(hasItem(HotelConstants.DB_ID.intValue())))
				.andExpect(jsonPath("$.content.[*].name").value(hasItem(HOTEL_NAME)))
				.andExpect(jsonPath("$.content.[*].description").value(hasItem(HOTEL_DESC)))
				.andExpect(jsonPath("$.content.[*].averageRating").value(hasItem(AVERAGE_RATING)));
	}

	@Test
	public void testGetHotel() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/get/" + HotelConstants.DB_ID.intValue())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.name").value(HOTEL_NAME))
				.andExpect(jsonPath("$.description").value(HOTEL_DESC))
				.andExpect(jsonPath("$.averageRating").value(AVERAGE_RATING));
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddHotel() throws Exception {
		Hotel hotel = new Hotel();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		hotel.setName(NEW_HOTEL_NAME);
		hotel.setDescription(NEW_HOTEL_DESCRIPTION);
		hotel.setLocation(location);
		String json = TestUtil.json(hotel);
		this.mockMvc.perform(post(URL_PREFIX + "/add").header("Authorization", "Bearer " + accessTokenSysAdmin)
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testEditHotel() throws Exception {
		Hotel hotel = new Hotel();
		Location location = new Location();
		location.setAddress(LOCATION_ADDRESS);
		location.setCity(LOCATION_CITY);
		location.setCountry(LOCATION_COUNTRY);
		location.setLon(LOCATION_LON);
		location.setLat(LOCATION_LAT);

		hotel.setId(HotelConstants.DB_ID.intValue());
		hotel.setName(NEW_HOTEL_NAME);
		hotel.setDescription(NEW_HOTEL_DESCRIPTION);
		hotel.setLocation(location);

		String json = TestUtil.json(hotel);
		this.mockMvc.perform(put(URL_PREFIX + "/edit/" + HotelConstants.DB_ID.intValue())
				.header("Authorization", "Bearer " + accessTokenHotelAdmin).contentType(contentType).content(json))
				.andExpect(status().isOk());

	}

	@Test
	@Transactional
	@Rollback(true)
	public void testSearchHotel() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/search?name=" + HOTEL_NAME + "&address=&checkInDay=&checkOutDate="))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteRoom() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteRoom/" + HotelConstants.DB_ID + "/" + HotelConstants.ROOM_ID).header("Authorization", "Bearer " + accessTokenHotelAdmin))
		.andExpect(status().isOk()); 
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteRoomExp() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteRoom/" + 102 + "/" + HotelConstants.ROOM_ID).header("Authorization", "Bearer " + accessTokenHotelAdmin))
		.andExpect(status().isUnauthorized()); 
	}
		
	@Test
	@Transactional
	@Rollback(true)
	public void testAddRoom() throws Exception {		
		Room room = new Room(null, 4, 4, 4, 15.0, "Apartman");
		String json = TestUtil.json(room);
		this.mockMvc.perform(post(URL_PREFIX + "/addRoom/100").header("Authorization", "Bearer " + accessTokenHotelAdmin)
				.contentType(contentType).content(json)).andExpect(status().isCreated());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddAdditionalService() throws Exception {		
		AdditionalService as = new AdditionalService("AsName", "AsDesc", 2.5);
		String json = TestUtil.json(as);
		this.mockMvc.perform(post(URL_PREFIX + "/addAdditionalService/100").header("Authorization", "Bearer " + accessTokenHotelAdmin)
				.contentType(contentType).content(json)).andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteServiceExp() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteAdditionalService/101"  + "/100").header("Authorization", "Bearer " + accessTokenHotelAdmin))
		.andExpect(status().isUnauthorized()); 
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteService() throws Exception {
		this.mockMvc.perform(delete(URL_PREFIX + "/deleteAdditionalService/" +  HotelConstants.DB_ID + "/100").header("Authorization", "Bearer " + accessTokenHotelAdmin))
		.andExpect(status().isOk()); 
	}
}
