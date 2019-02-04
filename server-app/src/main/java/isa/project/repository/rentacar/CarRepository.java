package isa.project.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.rentacar.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

	@Query("SELECT c FROM Car c WHERE c.rentACarCompany.id = :id AND c.isFast = true")
	Iterable<Car> findFast(@Param("id") Integer id);
	
}
