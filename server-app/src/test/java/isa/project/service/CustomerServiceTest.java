package isa.project.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import isa.project.model.users.Customer;
import isa.project.repository.users.CustomerRepository;
import isa.project.service.users.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private Customer customer;
	
	@InjectMocks
	private CustomerService customerService;
	
	@Mock
    private PasswordEncoder passwordEncoder;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testRegister() {
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer cust = customerService.registerCustomer(customer);
    
        cust.setPassword(passwordEncoder.encode(cust.getPassword()));
        assertThat(cust, is(equalTo(customer)));
	}

}
