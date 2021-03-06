package isa.project.repository.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import isa.project.model.users.HotelAdmin;

public interface HotelAdminRepository extends JpaRepository<HotelAdmin, Integer>{
	Optional<HotelAdmin> findByEmail( String email );

}
