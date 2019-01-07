package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.AirCompanyAdmin;
import isa.project.repository.users.AirCompanyAdminRepository;

@Service
public class AirCompanyAdminService {

	@Autowired
	private AirCompanyAdminRepository airCompanyAdminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* For registration */
	public AirCompanyAdmin registerAvioCompanyAdmin(AirCompanyAdmin airCompanyAdmin)
	{
		airCompanyAdmin.setPassword(passwordEncoder.encode(airCompanyAdmin.getPassword()));
		return airCompanyAdminRepository.save(airCompanyAdmin);
	}
	
	public Iterable<AirCompanyAdmin> findAll(){
		return airCompanyAdminRepository.findAll();
	}
	
	public Optional<AirCompanyAdmin> findCustomer(Integer id){
		return airCompanyAdminRepository.findById(id);
	}
	
	public AirCompanyAdmin saveAvioCompanyAdmin(AirCompanyAdmin admin) {
		return airCompanyAdminRepository.save(admin);
	}
}
