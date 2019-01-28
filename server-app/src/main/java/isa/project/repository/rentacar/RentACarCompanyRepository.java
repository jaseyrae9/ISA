package isa.project.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.rentacar.RentACarCompany;

public interface RentACarCompanyRepository extends JpaRepository<RentACarCompany, Integer> {

	@Query("SELECT r FROM RentACarCompany r WHERE r.name LIKE %:name% AND r.location.address LIKE %:address%")
	Iterable<RentACarCompany> searchNameAndAddress(@Param("name") String name, @Param("address") String address);

}
