package isa.project.service.rentacar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.rentacar.RentACarCompany;
import isa.project.repository.rentacar.RentACarCompanyRepository;

@Service
public class RentACarCompanyService {

	@Autowired
	private RentACarCompanyRepository rentACarRepository;
	
	public Iterable<RentACarCompany> findAll(){
		return rentACarRepository.findAll();
	}
	
	public Optional<RentACarCompany> findRentACarCompany(Integer id){
		return rentACarRepository.findById(id);
	}
	
	public RentACarCompany saveRentACarCompany(RentACarCompany company) {
		return rentACarRepository.save(company);
	}
	
}
