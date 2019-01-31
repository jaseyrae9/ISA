package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.security.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>{

	Optional<VerificationToken> findByToken(String token);
}
