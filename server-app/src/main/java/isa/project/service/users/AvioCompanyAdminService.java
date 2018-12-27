package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.AirCompanyAdmin;
import isa.project.model.users.HotelAdmin;
import isa.project.repository.users.AvioCompanyAdminRepository;

@Service
public class AvioCompanyAdminService {

	@Autowired
	private AvioCompanyAdminRepository avioCompanyAdminRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/* For registration */
	public AirCompanyAdmin registerAvioCompanyAdmin(AirCompanyAdmin airCompanyAdmin)
	{
		airCompanyAdmin.setPassword(passwordEncoder.encode(airCompanyAdmin.getPassword()));
		return avioCompanyAdminRepository.save(airCompanyAdmin);
	}
	
	public Iterable<AirCompanyAdmin> findAll(){
		return avioCompanyAdminRepository.findAll();
	}
	
	public Optional<AirCompanyAdmin> findCustomer(Integer id){
		return avioCompanyAdminRepository.findById(id);
	}
	
	public AirCompanyAdmin saveAvioCompanyAdmin(AirCompanyAdmin admin) {
		return avioCompanyAdminRepository.save(admin);
	}
}
