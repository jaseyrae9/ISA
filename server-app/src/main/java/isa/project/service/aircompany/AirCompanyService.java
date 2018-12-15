package isa.project.service.aircompany;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.aircompany.AirCompany;
import isa.project.respository.aircompany.AirCompanyRespository;

@Service
public class AirCompanyService {
	@Autowired
	private AirCompanyRespository airCompanyRespository;
	
	public Iterable<AirCompany> findAll(){
		return airCompanyRespository.findAll();
	}
	
	public Optional<AirCompany> findAircompany(Integer id) {
		return airCompanyRespository.findById(id);
	}
	
	public AirCompany saveAirCompany(AirCompany company) {
		return airCompanyRespository.save(company);
	}
	
}
