package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isa.project.model.hotel.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

	@Query("SELECT r FROM Room r WHERE r.hotel.id = :id AND r.isFast = true")
	Iterable<Room> findFast(@Param("id") Integer id);
}
