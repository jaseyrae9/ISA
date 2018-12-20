package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
	Optional<Authority> findByName( String name );
}
