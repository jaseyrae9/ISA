package isa.project.controller.rentacar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value="rent_a_car_companies")
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
	 * Returns data about rent a car company with selected id.
	 * 
	 * @param id - id of rent a car company
	 * @return
	 * @throws ResourceNotFoundException - if there is no hotel with selected id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCarCompany(@PathVariable Integer id) throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(id);
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car company not found");
		}
		return new ResponseEntity<>(carCompany.get(), HttpStatus.OK); // Maybe, DTO
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
		if(!opt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//set name and description
		opt.ifPresent( rentACarCompany -> {
			rentACarCompany.setName(company.getName());
			rentACarCompany.setDescription(company.getDescription());
		});
		return new ResponseEntity<>(new RentACarCompanyDTO(rentACarCompanyService.saveRentACarCompany(opt.get())), HttpStatus.OK);	
	}
	
	/**
	 * Adds new car to rent a car company. 
	 * @param car
	 * @return
	 */
	@RequestMapping(value="/addCar/{companyId}",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<CarDTO> addCar(@RequestBody CarDTO carDTO, @PathVariable Integer companyId) throws ResourceNotFoundException{
		Car car = new Car(carDTO.getBrand(), carDTO.getModel(), carDTO.getYearOfProduction(), carDTO.getSeatsNumber(), carDTO.getDoorsNumber(), carDTO.getPrice());
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}
		
		carCompany.get().getCars().add(car);
		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		System.out.println("Dodavanje auta!");
		return new ResponseEntity<>(new CarDTO(car), HttpStatus.CREATED);	
	}
	
	/**
	 * Edit car of rent a car company. 
	 * @param car
	 * @return
	 */
	@RequestMapping(value="/editCar/{companyId}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<CarDTO> editCar(@RequestBody CarDTO carDTO, @PathVariable Integer companyId) throws ResourceNotFoundException{
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(companyId);
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}
		
		for(Car c : carCompany.get().getCars())
		{
			if(c.getId().equals(carDTO.getId()))
			{
				System.out.println("nasao auto");
				c.setBrand(carDTO.getBrand());
				c.setModel(carDTO.getModel());
				c.setYearOfProduction(carDTO.getYearOfProduction());
				c.setSeatsNumber(carDTO.getSeatsNumber());
				c.setDoorsNumber(carDTO.getDoorsNumber());
				c.setPrice(carDTO.getPrice());
			}
		}
				
		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		return new ResponseEntity<>(carDTO, HttpStatus.OK);	
	}
	
	
}
