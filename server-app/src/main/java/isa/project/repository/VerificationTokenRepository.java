package isa.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.VerificationToken;
import isa.project.model.users.Customer;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>{

	VerificationToken findByToken(String token);
	
	VerificationToken findByCustomer(Customer customer);
}
