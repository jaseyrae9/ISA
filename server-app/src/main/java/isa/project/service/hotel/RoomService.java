package isa.project.service.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.hotel.Room;
import isa.project.repository.hotel.RoomRepository;
@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	public Iterable<Room> findAllFast(Integer id) {
		return roomRepository.findFast(id);
	}

	public Optional<Room> findRoom(Integer id) {
		return roomRepository.findById(id);
	}
	
	public List<Room> findAll() {
		return roomRepository.findAll();
	}

	public Room saveRoom(Room room) {
		System.out.println("room " + room.getId());
		return roomRepository.save(room);
	}
}
