package isa.project.repository.aircompany;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.aircompany.Flight;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
	/**
	 * Pronalazi aktive i in_progress letove aviokompanije.
	 * @param id - oznaka aviokompanije
	 * @param pageable - stranica
	 * @return
	 */
	@Query("SELECT f FROM Flight f WHERE f.airCompany.id = :id and (f.status = 1 or f.status = 0)  order by f.startDateAndTime desc")
	Page<Flight> getFlights(@Param("id") Integer id, Pageable pageable);

	/**
	 * Pronalazi aktivne letove.
	 * 
	 * @param id - oznaka aviokompanije.
	 * @param pageable - stranica
	 * @return
	 */
	@Query("SELECT f FROM Flight f WHERE f.airCompany.id = :id and f.status = 1  order by f.startDateAndTime desc")
	Page<Flight> getActiveFlights(@Param("id") Integer id, Pageable pageable);
}