package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.dto.users.UserProfileDTO;
import isa.project.model.CustomUserDetails;
import isa.project.model.users.User;
import isa.project.repository.users.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Email not found"));
		return optionalUser.map(CustomUserDetails::new).get();
	}

	/**
	 * @param username
	 * @return user with selected email
	 */
	public Optional<User> loadByEmail(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		return optionalUser;
	}

	/**
	 * Change password of currently logged in user. Saves changes in database.
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @return false if old password is not correct, true if password change is
	 *         successful.
	 */
	public boolean changePassword(String oldPassword, String newPassword) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} catch (Exception e) {
			return false;
		}

		User user = userRepository.findByEmail(username).get();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		return true;
	}

	/**
	 * Updates information of currently logged in user. Updates user's first name,
	 * last name, address and phone number to match the ones in DTO object. Saves
	 * changes to database.
	 * 
	 * @param profileInfo
	 */
	public User updateProfileInfo(UserProfileDTO profileInfo) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String email = currentUser.getName();
		User user = userRepository.findByEmail(email).get();
		user.setFirstName(profileInfo.getFirstName());
		user.setLastName(profileInfo.getLastName());
		user.setAddress(profileInfo.getAddress());
		user.setPhoneNumber(profileInfo.getPhoneNumber());
		return userRepository.save(user);
	}
}
