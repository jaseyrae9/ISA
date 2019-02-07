package isa.project.controller.users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.reservations.FriendInviteDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.security.TokenUtils;
import isa.project.service.users.FriendInvitesService;

@RestController
@RequestMapping(value = "customers")
public class FriendInvitesController {
	@Autowired
	private FriendInvitesService friendInvitesService;
	@Autowired
	private TokenUtils tokenUtils;
	
	/**
	 * Salje sve pozivnice namenje korisniku.
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/getInvites", method = RequestMethod.GET)
	public ResponseEntity<?> getFriendInvites(HttpServletRequest request){
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return ResponseEntity.ok(friendInvitesService.getAllInvites(email));
	}
	
	/**
	 * Prihvata pozivnicu.
	 * @param request
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws RequestDataException
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/acceptInvite/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> acceptInvite(HttpServletRequest request, @PathVariable Integer id) throws ResourceNotFoundException, RequestDataException{
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return ResponseEntity.ok(new FriendInviteDTO(friendInvitesService.acceptInvite(id, email)));
	}
	
	/**
	 * Odbija pozivnicu.
	 * @param request
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws RequestDataException
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/refuseInvite/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> refuseInvite(HttpServletRequest request, @PathVariable Integer id) throws ResourceNotFoundException, RequestDataException{
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return ResponseEntity.ok(new FriendInviteDTO(friendInvitesService.refuseInviteWithId(id, email)));
	}
}
