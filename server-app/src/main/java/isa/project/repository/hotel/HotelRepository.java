package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.hotel.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

	@Query("SELECT h FROM Hotel h WHERE lower(h.name) LIKE %:name% AND lower(h.location.address) LIKE %:address%")
	Iterable<Hotel> findSearchAll(@Param("name") String name,@Param("address") String address);

}
