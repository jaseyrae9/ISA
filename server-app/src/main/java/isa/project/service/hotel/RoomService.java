package isa.project.service.hotel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.hotel.RoomDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Room editRoom(Hotel hotel, RoomDTO roomDTO) throws RequestDataException {
		for (Room r : hotel.getRooms()) {
			if (r.getId().equals(roomDTO.getId())) {
				if (notReserved(r)) {
					r.setRoomNumber(roomDTO.getRoomNumber());
					r.setFloor(roomDTO.getFloor());
					r.setNumberOfBeds(roomDTO.getNumberOfBeds());
					r.setPrice(roomDTO.getPrice());
					r.setType(roomDTO.getType());
					return r;
				} else
					throw new RequestDataException("Can't edit reserved room!");
			}
		}
		return null;
	}

	public Boolean notReserved(Room r) {
		Date today = new Date();
		for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
			RoomReservation rr = srr.getRoomReservation();
			if (rr.getActive() && today.compareTo(rr.getCheckOutDate()) < 0) {
				return false;
			}
		}
		return true;
	}
}
