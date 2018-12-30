package isa.project.controller.users;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.ChangePasswordDTO;
import isa.project.dto.users.UserProfileDTO;
import isa.project.model.json.AuthenticationResponse;
import isa.project.model.users.User;
import isa.project.security.TokenUtils;
import isa.project.service.users.CustomUserDetailsService;

@RestController
@RequestMapping(value = "/profile")
public class UserProfileController {
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * Uses token to determine which user is logged in. Returns information about
	 * profile of logged in user. Information contains: email, first name, last
	 * name, phone number, address and is email confirmed.
	 * 
	 * @return info about currently logged in user.
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public ResponseEntity<?> getProfileInfo(HttpServletRequest request) {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Optional<User> optionalUser = userDetailsService.loadByEmail(email);
		return new ResponseEntity<UserProfileDTO>(new UserProfileDTO(optionalUser.get()), HttpStatus.OK);
	}

	/**
	 * Change password of currently logged in user. Checks if old password is
	 * correct.
	 * 
	 * @return token with new password if old password is correct, bad request error
	 *         if old password is incorrect
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO passwordInfo,
			HttpServletRequest request, Device device) {
		if (userDetailsService.changePassword(passwordInfo.getOldPassword(), passwordInfo.getNewPassword())) {
			String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			String token = this.tokenUtils.generateToken(userDetails, device);
			return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Old password is incorrect.", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Enables user to update profile information: first and last name, address and
	 * phone number.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public ResponseEntity<?> updateProfile(@Valid @RequestBody UserProfileDTO userProfileDTO,
			HttpServletRequest request) {
		try {
			User user = userDetailsService.updateProfileInfo(userProfileDTO);
			return new ResponseEntity<>(new UserProfileDTO(user), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			// phone number is not unique
			return new ResponseEntity<String>("Phone number is already used.", HttpStatus.BAD_REQUEST);
		}
	}

}
