package isa.project.controller.users;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.FriendshipDTO;
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
	 * Šalje zahtev za prijateljstvo od ulogovan korisnika, ka drugom korisniku.
	 * 
	 * @param to      - kome se zahtev šalje
	 * @param request
	 * @return - DTO prijateljstva
	 * @throws ResourceNotFoundException - ako nije pronađena osoba kojoj se šalje
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/sendRequest/{to}", method = RequestMethod.GET)
	public ResponseEntity<?> sendFriendRequest(@PathVariable Integer to, HttpServletRequest request)
			throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Friendship friendship = friendshipService.sendFriendRequest(email, to);
		return ResponseEntity.ok(new FriendshipDTO(friendship));
	}

	/**
	 * Vraća listu zahteva za prijateljstvo trenutnog korisnika.
	 * 
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/friendRequests", method = RequestMethod.GET)
	public ResponseEntity<?> getAllFriendRequests(HttpServletRequest request) throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		List<Friendship> requests = friendshipService.getFriendshipRequests(email);
		List<FriendshipDTO> requestsDTO = new ArrayList<>();
		for (Friendship req : requests) {
			requestsDTO.add(new FriendshipDTO(req));
		}
		return ResponseEntity.ok(requestsDTO);
	}

}
