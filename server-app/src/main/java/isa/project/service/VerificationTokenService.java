package isa.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.VerificationToken;
import isa.project.model.users.Customer;
import isa.project.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    @Autowired
    VerificationTokenRepository tokenRepository;

    public void createVerificationToken(Customer customer, String token) {
        tokenRepository.save(new VerificationToken(token, customer));
    }

    
    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}