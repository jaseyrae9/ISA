package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.users.AvioCompanyAdmin;
import isa.project.model.users.RentACarAdmin;
import isa.project.repository.users.RentACarAdminRepository;

@Service
public class RentACarAdminService {
	
	@Autowired
	private RentACarAdminRepository rentACarRepository;
	
	public Iterable<RentACarAdmin> findAll(){
		return rentACarRepository.findAll();
	}
	
	public Optional<RentACarAdmin> findCustomer(String username){
		return rentACarRepository.findById(username);
	}
	
	public RentACarAdmin saveAvioCompanyAdmin(RentACarAdmin admin) {
		return rentACarRepository.save(admin);
	}
}
