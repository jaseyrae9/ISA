package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.Customer;
import isa.project.repository.users.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* For registration */
	public Customer registerCustomer(Customer customer)
	{
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		return customerRepository.save(customer);
	}
	
	public Iterable<Customer> findAll(){
		return customerRepository.findAll();
	}	
	
	public Customer saveCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public Optional<Customer> findCustomer(String email){
		return customerRepository.findByEmail(email);
	}
	
	/**
	 * Pronalazi korisnika sa zadatim id-om.
	 * @param id
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Customer findCustomerById(Integer id) throws ResourceNotFoundException {
		Optional<Customer> customer = customerRepository.findById(id);
		if(!customer.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "User not found.");
		}
		return customer.get();
	}
	
	/**
	 * Pronalazi korisnika sa zadatom email adresom.
	 * @param email
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public Customer findCustomerByEmail(String email) throws ResourceNotFoundException {
		Optional<Customer> customer = customerRepository.findByEmail(email);
		if(!customer.isPresent()) {
			throw new ResourceNotFoundException(email, "User not found.");
		}
		return customer.get();
	}
}
