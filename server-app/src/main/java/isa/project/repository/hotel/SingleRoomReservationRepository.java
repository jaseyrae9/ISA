package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.hotel.SingleRoomReservation;

public interface SingleRoomReservationRepository extends JpaRepository <SingleRoomReservation, Integer>{

}
