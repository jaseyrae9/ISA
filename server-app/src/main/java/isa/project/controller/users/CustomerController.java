package isa.project.controller.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.users.CustomerDTO;
import isa.project.model.users.Customer;
import isa.project.service.users.CustomerService;

@RestController
@RequestMapping(value="server/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	/**
	 * Returns DTO objects for customers. Objects contain username, password, first name, last name, email, phone number, address and information about confirmation mail.
	 * @return information about all customers.
	 */
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
		Iterable<Customer> customers = customerService.findAll();
		
		//convert companies to DTO
		List<CustomerDTO> ret = new ArrayList<>();
		for(Customer customer: customers) {
			ret.add(new CustomerDTO(customer));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/**
	 * Adds new customer. 
	 * @param customer
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customerDTO){
		Customer customer = new Customer(customerDTO.getUsername(), customerDTO.getPassword(), customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getEmail(), customerDTO.getPhoneNumber(), customerDTO.getAddress());
		
		return new ResponseEntity<>(new CustomerDTO(customerService.saveCustomer(customer)), HttpStatus.CREATED);
	}

	
}
