package isa.project.repository.aircompany;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.aircompany.FlightReservation;

public interface FlightReservationRepository extends JpaRepository<FlightReservation, Integer> {

}
