package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.users.Authority;
import isa.project.repository.users.AuthorityRepository;

@Service
public class AuthorityService {
	@Autowired
	AuthorityRepository authorityRepository;
	
	public Optional<Authority> findByName(String name){
		return authorityRepository.findByName(name);
	}
	
	public Authority saveAuthority(Authority authority) {
		return authorityRepository.save(authority);
	}
}
