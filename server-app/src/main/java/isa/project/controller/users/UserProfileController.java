package isa.project.controller.users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.UserProfileDTO;
import isa.project.model.users.User;
import isa.project.security.TokenUtils;
import isa.project.service.users.UserService;

@RestController
@RequestMapping(value="/profile")
public class UserProfileController {
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Uses token to determine which user is logged in. Returns information about profile of logged in user.
	 * Information contains: username, first name, last name, email, phone number, address and is email confirmed.
	 * @return info about currently logged in profile
	 */
	@RequestMapping(value="/info", method = RequestMethod.GET)
	public ResponseEntity<UserProfileDTO> getProfileInfo(HttpServletRequest request){
		String username = tokenUtils.getUsernameFromToken(tokenUtils.getToken(request));
		User user = userService.findByUsername(username);
		return new ResponseEntity<UserProfileDTO>(new UserProfileDTO(user), HttpStatus.OK);
	}
}
