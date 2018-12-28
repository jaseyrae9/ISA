package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.Customer;
import isa.project.repository.users.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/* For registration */
	public Customer registerCustomer(Customer customer)
	{
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		return customerRepository.save(customer);
	}
	
	public Iterable<Customer> findAll(){
		return customerRepository.findAll();
	}
	
	public Optional<Customer> findCustomer(Integer id){
		return customerRepository.findById(id);
	}
	
	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
}
