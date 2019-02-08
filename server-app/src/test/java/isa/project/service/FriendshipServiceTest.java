package isa.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import isa.project.constants.FriendshipServiceConstants;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.Customer;
import isa.project.model.users.friendship.Friendship;
import isa.project.repository.users.CustomerRepository;
import isa.project.repository.users.FriendshipRepository;
import isa.project.service.users.FriendshipService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendshipServiceTest {
	@Mock
	private FriendshipRepository friendshipRepository;
	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private FriendshipService friendshipService;
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@SuppressWarnings("deprecation")
	@Test
	@Transactional
	@Rollback(true)
	public void testGetFriendshipRequests() {		
		PageRequest pageRequest = new PageRequest(1, 2);
		Customer customer = new Customer();
		customer.setId(FriendshipServiceConstants.FR_CUSTOMER_ID);
		customer.setEmail(FriendshipServiceConstants.FR_CUSTOMER_EMAIL);
		Friendship friendship = new Friendship();
		when(friendshipRepository.findFriendshipRequests(FriendshipServiceConstants.FR_CUSTOMER_ID, pageRequest)).thenReturn(new PageImpl<Friendship>(Arrays.asList(friendship).subList(0, 1), pageRequest, 1));
		when(customerRepository.findByEmail(FriendshipServiceConstants.FR_CUSTOMER_EMAIL)).thenReturn(Optional.of(customer));
		
		Page<Friendship> friendships = friendshipService.getFriendshipRequests(FriendshipServiceConstants.FR_CUSTOMER_EMAIL, pageRequest);
		assertThat(friendships).hasSize(1);
		verify(friendshipRepository, times(1)).findFriendshipRequests(FriendshipServiceConstants.FR_CUSTOMER_ID, pageRequest);
        verifyNoMoreInteractions(friendshipRepository);
        verify(customerRepository, times(1)).findByEmail(FriendshipServiceConstants.FR_CUSTOMER_EMAIL);
        verifyNoMoreInteractions(customerRepository);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testSendFriendRequest() throws ResourceNotFoundException, RequestDataException {
		Customer from = new Customer();
		from.setEmail(FriendshipServiceConstants.FR_FROM_EMAIL);
		from.setId(FriendshipServiceConstants.FR_FROM_ID);
		Customer to = new Customer();
		to.setId(FriendshipServiceConstants.FR_TO_ID);
		
		when(customerRepository.findByEmail(FriendshipServiceConstants.FR_FROM_EMAIL)).thenReturn(Optional.of(from));
		when(customerRepository.findById(FriendshipServiceConstants.FR_TO_ID)).thenReturn(Optional.of(to));
		when(friendshipRepository.findFriendship(FriendshipServiceConstants.FR_FROM_ID, FriendshipServiceConstants.FR_TO_ID)).thenReturn(Optional.ofNullable(null));
		
		friendshipService.sendFriendRequest(FriendshipServiceConstants.FR_FROM_EMAIL, FriendshipServiceConstants.FR_TO_ID);
		verify(friendshipRepository, times(1)).findFriendship(FriendshipServiceConstants.FR_FROM_ID, FriendshipServiceConstants.FR_TO_ID);
		verify(customerRepository, times(1)).findByEmail(FriendshipServiceConstants.FR_FROM_EMAIL);
		verify(customerRepository, times(1)).findById(FriendshipServiceConstants.FR_TO_ID);		
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAcceptRequest() throws ResourceNotFoundException {
		exceptionRule.expect(ResourceNotFoundException.class);
	    exceptionRule.expectMessage("You don't have friend request from this user.");
	    
		Customer from = new Customer();
		from.setEmail(FriendshipServiceConstants.FR_FROM_EMAIL);
		from.setId(FriendshipServiceConstants.FR_FROM_ID);
		Customer to = new Customer();
		to.setId(FriendshipServiceConstants.FR_TO_ID);
		Friendship friendship = new Friendship();
		friendship.setActive(false);
		
		Friendship friendshipSaved = new Friendship();
		friendshipSaved.setActive(true);
		
		when(customerRepository.findByEmail(FriendshipServiceConstants.FR_FROM_EMAIL)).thenReturn(Optional.of(from));
		when(customerRepository.findById(FriendshipServiceConstants.FR_TO_ID)).thenReturn(Optional.of(to));
		when(friendshipRepository.findFriendship(2, 1)).thenReturn(Optional.of(friendship));
		
		when(friendshipRepository.save(friendshipSaved)).thenReturn(friendshipSaved);
		
		friendshipService.acceptRequest(FriendshipServiceConstants.FR_TO_ID, FriendshipServiceConstants.FR_FROM_EMAIL);
	}
	
	
	
	
	
}
