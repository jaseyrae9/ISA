package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.SystemAdmin;
import isa.project.repository.users.SystemAdminRepository;

@Service
public class SystemAdminService {
	
	@Autowired
	private SystemAdminRepository sysAdminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Iterable<SystemAdmin> findAll() {
		return sysAdminRepository.findAll();
	}
	
	public Optional<SystemAdmin> findCustomer(Integer id) {
		return sysAdminRepository.findById(id);
	}
	
	public SystemAdmin saveSystemAdmin(SystemAdmin admin) {
		return sysAdminRepository.save(admin);
	}

	public SystemAdmin registerSystemAdmin(SystemAdmin systemAdmin) {
		systemAdmin.setPassword(passwordEncoder.encode(systemAdmin.getPassword()));
		return sysAdminRepository.save(systemAdmin);
	}
}
