package isa.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>{

	VerificationToken findByToken(String token);
}