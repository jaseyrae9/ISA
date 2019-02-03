package isa.project.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import static isa.project.constants.HotelConstants.HOTEL_COUNT;
import static isa.project.constants.HotelConstants.HOTEL_SIZE_COUNT;
import static isa.project.constants.HotelConstants.HOTEL_NAME;
import static isa.project.constants.HotelConstants.HOTEL_DESC;
import static isa.project.constants.HotelConstants.AVERAGE_RATING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import isa.project.constants.HotelConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelControllerTest {

	private static final String URL_PREFIX = "/hotels";
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
		.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content",hasSize(HOTEL_SIZE_COUNT)))
		.andExpect(jsonPath("$.content.[*].id").value(hasItem(HotelConstants.DB_ID.intValue())))
		.andExpect(jsonPath("$.content.[*].name").value(hasItem(HOTEL_NAME)))
		.andExpect(jsonPath("$.content.[*].description").value(hasItem(HOTEL_DESC)))
		.andExpect(jsonPath("$.content.[*].averageRating").value(hasItem(AVERAGE_RATING)));
	}
	
	@Test
	public void testGetHotel() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/get/" + HotelConstants.DB_ID.intValue())).andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("$.name").value(HOTEL_NAME))
		.andExpect(jsonPath("$.description").value(HOTEL_DESC))
		.andExpect(jsonPath("$.averageRating").value(AVERAGE_RATING));
	}
	
}