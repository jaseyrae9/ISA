package isa.project.controller.rentacar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value="server/rent_a_car_comapnies")
public class RentACarCompanyController {

	@Autowired
	private RentACarCompanyService rentACarCompanyService;
	
	
	/**
	 * Returns DTO objects for rent a car companies. Objects contain id, name, address and description.
	 * @return information about all rent a car companies.
	 */
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<RentACarCompanyDTO>> getAllRentACarCompanies(){
		Iterable<RentACarCompany> companies = rentACarCompanyService.findAll();
		
		//convert companies to DTO
		List<RentACarCompanyDTO> ret = new ArrayList<>();
		for(RentACarCompany company: companies) {
			ret.add(new RentACarCompanyDTO(company));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	/**
	 * Adds new rent a car company. 
	 * @param company
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<RentACarCompanyDTO> addRentACarCompany(@RequestBody RentACarCompanyDTO rentACarCompanyDTO){
		RentACarCompany company = new RentACarCompany(rentACarCompanyDTO.getName(), rentACarCompanyDTO.getDescription());
		return new ResponseEntity<>(new RentACarCompanyDTO(rentACarCompanyService.saveRentACarCompany(company)), HttpStatus.CREATED);	
	}
	

	/**
	 * Edit existing rent a car company.
	 * @param company
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<RentACarCompanyDTO> editRentACarCompany(@RequestBody RentACarCompanyDTO company){
		//rent a car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(company.getId());
		
		//rent a car company is not found
		if( opt.isPresent() == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//set name and description
		opt.ifPresent( rentACarCompany -> {
			rentACarCompany.setName(company.getName());
			rentACarCompany.setDescription(company.getDescription());
		});
		return new ResponseEntity<>(new RentACarCompanyDTO(rentACarCompanyService.saveRentACarCompany(opt.get())), HttpStatus.OK);	
	}
}
