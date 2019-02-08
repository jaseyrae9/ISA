package isa.project.controller;

import static isa.project.constants.FriendshipControllerConstants.CUSTOMER_EMAIL;
import static isa.project.constants.FriendshipControllerConstants.CUSTOMER_PASSWORD;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import isa.project.constants.FriendshipControllerConstants;
import isa.project.dto.users.AuthenticationResponse;
import isa.project.security.auth.JwtAuthenticationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FriendshipControlllerTest {
	private static final String URL_PREFIX = "/friendship";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Autowired
	private TestRestTemplate restTemplate;
	
	private String accessTokenCustomer;
	
	@PostConstruct
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.build();

		ResponseEntity<AuthenticationResponse> responseEntitySys = restTemplate.postForEntity("/customers/login",
				new JwtAuthenticationRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD), AuthenticationResponse.class);
		accessTokenCustomer = responseEntitySys.getBody().getToken();
	}
	
	@Test
	public void testGetFriends() throws Exception {
		this.mockMvc.perform(get(URL_PREFIX + "/friends?page=0&size=2").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(FriendshipControllerConstants.FRIENDS_COUNT)))
				.andExpect(jsonPath("$.content.[*].user2Id").value(hasItem(FriendshipControllerConstants.USER2_ID)))
				.andExpect(jsonPath("$.content.[*].user2Firstname").value(hasItem(FriendshipControllerConstants.USER2_NAME)))
				.andExpect(jsonPath("$.content.[*].user2Lastname").value(hasItem(FriendshipControllerConstants.USER2_LASTNAME)));
	}
	
	@Test
	public void testGetAllFriendRequests() throws Exception {
		this.mockMvc.perform(get(URL_PREFIX + "/friendRequests?page=0&size=2").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(FriendshipControllerConstants.REQUESTS_COUNT)))
				.andExpect(jsonPath("$.content.[*].user2Id").value(hasItem(FriendshipControllerConstants.R_USER2_ID)))
				.andExpect(jsonPath("$.content.[*].user2Firstname").value(hasItem(FriendshipControllerConstants.R_USER2_NAME)))
				.andExpect(jsonPath("$.content.[*].user2Lastname").value(hasItem(FriendshipControllerConstants.R_USER2_LASTNAME)));
	}
	
	@Test
	public void testSearchForFriends() throws Exception {
		this.mockMvc.perform(get(URL_PREFIX + "/search?searchTerm=mil&page=0&size=3").header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.content", hasSize(FriendshipControllerConstants.SEARCH_COUNT)))
				.andExpect(jsonPath("$.content.[*].user2Id").value(hasItem(FriendshipControllerConstants.S_USER2_ID)))
				.andExpect(jsonPath("$.content.[*].user2Firstname").value(hasItem(FriendshipControllerConstants.S_USER2_NAME)))
				.andExpect(jsonPath("$.content.[*].user2Lastname").value(hasItem(FriendshipControllerConstants.S_USER2_LASTNAME)));
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testSendFriendRequestSuccess() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/sendRequest/" + FriendshipControllerConstants.SEND_REQUEST_SUCCESS).header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.user2Id").value(FriendshipControllerConstants.SRS_USER2_ID))
				.andExpect(jsonPath("$.user2Firstname").value(FriendshipControllerConstants.SRS_USER2_NAME))
				.andExpect(jsonPath("$.user2Lastname").value(FriendshipControllerConstants.SRS_USER2_LASTNAME));
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testSendFriendRequestFail() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/sendRequest/" + FriendshipControllerConstants.SEND_REQUEST_FAIL).header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().is4xxClientError());
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testAcceptRequestSuccess() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/acceptRequest/" + FriendshipControllerConstants.ACCEPT_REQUEST_SUCCESS).header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.user2Id").value(FriendshipControllerConstants.A_USER2_ID))
				.andExpect(jsonPath("$.user2Firstname").value(FriendshipControllerConstants.A_USER2_NAME))
				.andExpect(jsonPath("$.user2Lastname").value(FriendshipControllerConstants.A_USER2_LASTNAME));
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testRefuseRequestSuccess() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/delete/" + FriendshipControllerConstants.DELETE_FRIEND_SUCCESS).header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().isOk());
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testRefuseRequestFail() throws Exception {
		mockMvc.perform(get(URL_PREFIX + "/delete/" + FriendshipControllerConstants.DELETE_FRIEND_FAIL).header("Authorization", "Bearer " + accessTokenCustomer))
				.andExpect(status().is4xxClientError());
	}
}
