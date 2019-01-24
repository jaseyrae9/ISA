package isa.project.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import isa.project.model.rentacar.CarReservation;

public interface CarReservationRepository extends JpaRepository<CarReservation, Integer>  {

}
