package isa.project.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.users.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

}
