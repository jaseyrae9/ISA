package isa.project.controller.users;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.FriendshipDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.friendship.Friendship;
import isa.project.security.TokenUtils;
import isa.project.service.users.FriendshipService;

@RestController
@RequestMapping(value = "/friendship")
public class FriendshipController {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private FriendshipService friendshipService;

	/**
	 * Sends friend request from logged in user to selected user.
	 * 
	 * @param to      - id of user receiving friend request
	 * @param request
	 * @return - friendship
	 * @throws ResourceNotFoundException - if user receiving request is not found
	 * @throws RequestDataException      - if user is trying to send friend request
	 *                                   to self, or friendship already exists
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/sendRequest/{to}", method = RequestMethod.GET)
	public ResponseEntity<?> sendFriendRequest(@PathVariable Integer to, HttpServletRequest request)
			throws ResourceNotFoundException, RequestDataException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Friendship friendship = friendshipService.sendFriendRequest(email, to);
		return ResponseEntity.ok(new FriendshipDTO(friendship, true));
	}

	/**
	 * Returns list of friend requests of logged in user.
	 * 
	 * @param request
	 * @return - DTOs od friend requests
	 * @throws ResourceNotFoundException
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/friendRequests", method = RequestMethod.GET)
	public ResponseEntity<?> getAllFriendRequests(HttpServletRequest request, Pageable page) {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));

		Page<Friendship> requests = friendshipService.getFriendshipRequests(email, page);
		List<FriendshipDTO> friendshipsDTO = new ArrayList<>();
		for (Friendship req : requests) {
			friendshipsDTO.add(new FriendshipDTO(req, false));
		}
		
		Page<FriendshipDTO> ret = new PageImpl<>(friendshipsDTO, requests.getPageable(), requests.getTotalElements());
		return ResponseEntity.ok(ret);
	}

	/**
	 * Accepts friendship request.
	 * 
	 * @param from    - id of user whose request is being accepted
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException - if user, or request are not found
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/acceptRequest/{from}", method = RequestMethod.GET)
	public ResponseEntity<?> acceptRequest(@PathVariable Integer from, HttpServletRequest request)
			throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Friendship friendship = friendshipService.acceptRequest(from, email);
		return ResponseEntity.ok(new FriendshipDTO(friendship, false));
	}

	/**
	 * Deletes existing friendship or friend request.
	 * 
	 * @param from    - id of other friend
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException - if other user, of friendship are not found
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/delete/{from}", method = RequestMethod.GET)
	public ResponseEntity<?> refuseRequest(@PathVariable Integer from, HttpServletRequest request)
			throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		friendshipService.deleteRequest(from, email);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Finds all friendships of logged in user.
	 * 
	 * @param request
	 * @return - DTOs of friendships
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/friends", method = RequestMethod.GET)
	public ResponseEntity<?> getFriends(HttpServletRequest request, Pageable page){
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Page<Friendship> friends = friendshipService.getFriendships(email, page);
		List<FriendshipDTO> friendshipsDTO = new ArrayList<>();
		for (Friendship freindship : friends) {
			friendshipsDTO.add(new FriendshipDTO(freindship, email.equals(freindship.getKey().getFrom().getEmail())));
		}
		
		Page<FriendshipDTO> ret = new PageImpl<>(friendshipsDTO, friends.getPageable(), friends.getTotalElements());
		return ResponseEntity.ok(ret);
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchForFriends(@RequestParam("searchTerm") String searchTerm, HttpServletRequest request, Pageable page){
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return ResponseEntity.ok(friendshipService.searchCustomers(searchTerm == null?"":searchTerm, email, page));
	}

}
