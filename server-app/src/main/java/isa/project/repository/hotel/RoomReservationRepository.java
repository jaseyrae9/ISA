package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.hotel.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Integer>{

}
