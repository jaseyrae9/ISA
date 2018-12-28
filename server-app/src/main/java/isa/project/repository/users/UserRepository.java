package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail( String email );
}
