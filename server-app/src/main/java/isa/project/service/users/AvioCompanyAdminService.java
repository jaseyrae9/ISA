package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.users.AvioCompanyAdmin;
import isa.project.model.users.Customer;
import isa.project.repository.users.AvioCompanyAdminRepository;

@Service
public class AvioCompanyAdminService {

	@Autowired
	private AvioCompanyAdminRepository avioCompanyAdminRepository;
	
	public Iterable<AvioCompanyAdmin> findAll(){
		return avioCompanyAdminRepository.findAll();
	}
	
	public Optional<AvioCompanyAdmin> findCustomer(String username){
		return avioCompanyAdminRepository.findById(username);
	}
	
	public AvioCompanyAdmin saveAvioCompanyAdmin(AvioCompanyAdmin admin) {
		return avioCompanyAdminRepository.save(admin);
	}
}
