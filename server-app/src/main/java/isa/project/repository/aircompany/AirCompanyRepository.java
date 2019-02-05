package isa.project.repository.aircompany;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import isa.project.model.aircompany.AirCompany;

public interface AirCompanyRepository extends JpaRepository<AirCompany, Integer> {

	@Query("SELECT count(*), MONTH(t.activeReservation.saleDate) FROM AirCompany a JOIN a.flights as f  JOIN f.tickets as t "
			+ "WHERE a.id = :id and t.status = 2 and YEAR(t.activeReservation.saleDate) = :year "
			+ "GROUP BY MONTH(t.activeReservation.saleDate)")
	public List<Object[]> getSoldTicketsPerMonth(Integer id, Integer year);
	
	@Query("SELECT count(*), DAY(t.activeReservation.saleDate) FROM AirCompany a JOIN a.flights as f  JOIN f.tickets as t "
			+ "WHERE a.id = :id and t.status = 2 and t.activeReservation.saleDate BETWEEN :start AND :end "
			+ "GROUP BY DAY(t.activeReservation.saleDate)")
	public List<Object[]> getSoldTicketsPerDay(Integer id, Date start, Date end);
	
	@Query("SELECT t.activeReservation.saleDate FROM AirCompany a JOIN a.flights as f  JOIN f.tickets as t "
			+ "WHERE a.id = :id and t.status = 2 and t.activeReservation.saleDate BETWEEN :start AND :end ")
	public List<Object> getSoldTicketsInPeriod(Integer id, Date start, Date end);
	
	@Query("SELECT sum(t.price) FROM AirCompany a JOIN a.flights as f  JOIN f.tickets as t "
			+ "WHERE a.id = :id and t.status = 2 and t.activeReservation.saleDate BETWEEN :start AND :end ")
	public Double getProfitInPeriod(Integer id, Date start, Date end);
}
