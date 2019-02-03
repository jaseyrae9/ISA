package isa.project.controller.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.exception_handlers.ResourceNotFoundException;

@RestController
@RequestMapping(value="reservation")
public class ReservationController {

	@RequestMapping(value="/rateCarCompany/{comapnyId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> rateCarCompany(@PathVariable Integer comapnyId, @PathVariable Integer rate) throws ResourceNotFoundException{
		
		
		return new ResponseEntity<>("", HttpStatus.OK);	
	}
	
}
