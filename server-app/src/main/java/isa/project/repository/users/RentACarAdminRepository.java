package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import isa.project.model.users.RentACarAdmin;

public interface RentACarAdminRepository extends JpaRepository<RentACarAdmin, Integer> {
	Optional<RentACarAdmin> findByEmail( String email );

}
