package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.SystemAdmin;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Integer> {
	Optional<SystemAdmin> findByEmail(String email);
}
