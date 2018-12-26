package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.AvioCompanyAdmin;
import isa.project.model.users.RentACarAdmin;
import isa.project.repository.users.RentACarAdminRepository;

@Service
public class RentACarAdminService {
	
	@Autowired
	private RentACarAdminRepository rentACarRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/* For registration */
	public RentACarAdmin registerRentACarCompanyAdmin(RentACarAdmin rentACarCompanyAdmin)
	{
		rentACarCompanyAdmin.setPassword(passwordEncoder.encode(rentACarCompanyAdmin.getPassword()));
		return rentACarRepository.save(rentACarCompanyAdmin);
	}	
	
	public Iterable<RentACarAdmin> findAll(){
		return rentACarRepository.findAll();
	}
	
	public Optional<RentACarAdmin> findCustomer(Integer id){
		return rentACarRepository.findById(id);
	}
	
	public RentACarAdmin saveAvioCompanyAdmin(RentACarAdmin admin) {
		return rentACarRepository.save(admin);
	}
}
