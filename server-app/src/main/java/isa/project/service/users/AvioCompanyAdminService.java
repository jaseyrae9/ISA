package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.AvioCompanyAdmin;
import isa.project.model.users.HotelAdmin;
import isa.project.repository.users.AvioCompanyAdminRepository;

@Service
public class AvioCompanyAdminService {

	@Autowired
	private AvioCompanyAdminRepository avioCompanyAdminRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/* For registration */
	public AvioCompanyAdmin registerAvioCompanyAdmin(AvioCompanyAdmin avioCompanyAdmin)
	{
		avioCompanyAdmin.setPassword(passwordEncoder.encode(avioCompanyAdmin.getPassword()));
		return avioCompanyAdminRepository.save(avioCompanyAdmin);
	}
	
	public Iterable<AvioCompanyAdmin> findAll(){
		return avioCompanyAdminRepository.findAll();
	}
	
	public Optional<AvioCompanyAdmin> findCustomer(Integer id){
		return avioCompanyAdminRepository.findById(id);
	}
	
	public AvioCompanyAdmin saveAvioCompanyAdmin(AvioCompanyAdmin admin) {
		return avioCompanyAdminRepository.save(admin);
	}
}
