package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.AirCompanyAdmin;

public interface AirCompanyAdminRepository extends JpaRepository<AirCompanyAdmin, Integer> {
	Optional<AirCompanyAdmin> findByEmail( String email );
}
