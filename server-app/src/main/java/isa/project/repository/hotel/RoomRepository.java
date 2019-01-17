package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.hotel.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

}
