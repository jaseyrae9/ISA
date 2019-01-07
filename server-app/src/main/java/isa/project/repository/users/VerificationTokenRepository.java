package isa.project.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.security.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>{

	VerificationToken findByToken(String token);
}
