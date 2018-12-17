package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.users.Customer;
import isa.project.repository.users.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public Iterable<Customer> findAll(){
		return customerRepository.findAll();
	}
	
	public Optional<Customer> findCustomer(String username){
		return customerRepository.findById(username);
	}
	
	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
}
