package isa.project.controller.aircompany;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.aircompany.FlightDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.service.aircompany.FlightService;

@RestController
@RequestMapping(value = "/aircompanies")
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@RequestMapping(value = "/getFlights/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFlights(@PathVariable Integer id, Pageable page){
		return new ResponseEntity<>(flightService.getFlights(id, page),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/addFlight/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> createAirplane(@PathVariable Integer id, @Valid @RequestBody FlightDTO flight)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.addNewFlight(id, flight), HttpStatus.CREATED);
	}
}
