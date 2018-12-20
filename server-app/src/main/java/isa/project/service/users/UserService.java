package isa.project.service.users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import isa.project.model.users.User;
import isa.project.repository.users.UserRepository;

public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
		Optional<User> u = userRepository.findByUsername(username);
		return u;
	}

	public Optional<User> findById(Integer id) throws AccessDeniedException {
		Optional<User> u = userRepository.findById(id);
		return u;
	}

	public List<User> findAll() throws AccessDeniedException {
		List<User> result = userRepository.findAll();
		return result;
	}

}
