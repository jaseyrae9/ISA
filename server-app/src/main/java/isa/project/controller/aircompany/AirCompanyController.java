package isa.project.controller.aircompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.aircompany.AirCompanyDTO;
import isa.project.model.aircompany.AirCompany;
import isa.project.service.aircompany.AirCompanyService;

@RestController
@RequestMapping(value="/aircompanies")
public class AirCompanyController {
	
	@Autowired
	private AirCompanyService airCompanyService;
	
	/**
	 * Returns DTO objects for air companies. Objects contain id, name, address and description.
	 * @return information about all air companies.
	 */
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<AirCompanyDTO>> getAllAvioCompanies(){
		Iterable<AirCompany> companies = airCompanyService.findAll();
		
		//convert companies to DTO
		List<AirCompanyDTO> ret = new ArrayList<>();
		for(AirCompany company:companies) {
			ret.add(new AirCompanyDTO(company));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	/**
	 * Adds new air company. 
	 * @param company
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes="application/json")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AirCompanyDTO> addAvioCompany(@RequestBody AirCompanyDTO company){
		AirCompany airCompany = new AirCompany(company.getName(), company.getDescription());
		return new ResponseEntity<>(new AirCompanyDTO(airCompanyService.saveAirCompany(airCompany)), HttpStatus.CREATED);	
	}
	
	/**
	 * Edit existing air company.
	 * @param company
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.PUT, consumes="application/json")
	//@PreAuthorize("hasRole('AIR" + company.getId() + "')")
	public ResponseEntity<AirCompanyDTO> editAvioCompany(@RequestBody AirCompanyDTO company){
		//air company must exist
		Optional<AirCompany> opt = airCompanyService.findAircompany(company.getId());
		
		//air company is not found
		if( opt.isPresent() == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//set name and description
		opt.ifPresent( airCompany -> {
			airCompany.setName(company.getName());
			airCompany.setDescription(company.getDescription());
		});
		return new ResponseEntity<>(new AirCompanyDTO(airCompanyService.saveAirCompany(opt.get())), HttpStatus.OK);	
	}

}
