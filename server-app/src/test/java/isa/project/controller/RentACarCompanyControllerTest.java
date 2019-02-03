package isa.project.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

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

import isa.project.constants.RentACarCompanyConstants;

import static isa.project.constants.RentACarCompanyConstants.AVERAGE_RATING;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_COUNT;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_DESCRIPTION;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_NAME;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarCompanyControllerTest {

	private static final String URL_PREFIX = "/rent_a_car_companies";

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
		      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	//rating_count, total_rating, location_id) values (100, 'Rent a Punto', 'Grande', 2, 8, 100);

	@Test
	  public void testGetAllCompanies() throws Exception {
	    mockMvc.perform(get(URL_PREFIX + "/allCompanies")).andExpect(status().isOk())
	    .andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(RENT_A_CAR_COMPANY_COUNT)))
	    .andExpect(jsonPath("$.[*].id").value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue())))
	    .andExpect(jsonPath("$.[*].name").value(hasItem(RENT_A_CAR_COMPANY_NAME)))
	    .andExpect(jsonPath("$.[*].averageRating").value(hasItem(AVERAGE_RATING)))
	    .andExpect(jsonPath("$.[*].description").value(hasItem(RENT_A_CAR_COMPANY_DESCRIPTION)))
	    .andExpect(jsonPath("$.[*].location.id").value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue())));
	  }
	
	@Test
	  public void testGetAllCompaniesPage() throws Exception {
	    mockMvc.perform(get(URL_PREFIX + "/all?page=0&size=2")).andExpect(status().isOk())
	    .andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(2)))
	    .andExpect(jsonPath("$.content.[*].id").value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID.intValue())))
	    .andExpect(jsonPath("$.content.[*].name").value(hasItem(RENT_A_CAR_COMPANY_NAME)))
	    .andExpect(jsonPath("$.content.[*].averageRating").value(hasItem(AVERAGE_RATING)))
	    .andExpect(jsonPath("$.content.[*].description").value(hasItem(RENT_A_CAR_COMPANY_DESCRIPTION)))
	    .andExpect(jsonPath("$.content.[*].location.id").value(hasItem(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue())));
	  }
	
	//get/{id}
	@Test
	  public void testGetCompany() throws Exception {
	    mockMvc.perform(get(URL_PREFIX + "/get/" + RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID)).andExpect(status().isOk())
	    .andExpect(content().contentType(contentType))
	    .andExpect(jsonPath("$.name").value(RENT_A_CAR_COMPANY_NAME))
	    .andExpect(jsonPath("$.averageRating").value(AVERAGE_RATING))
	    .andExpect(jsonPath("$.description").value(RENT_A_CAR_COMPANY_DESCRIPTION))
	    .andExpect(jsonPath("$.location.id").value(RentACarCompanyConstants.RENT_A_CAR_COMPANY_LOCATION_ID.intValue()));
	  }
}
