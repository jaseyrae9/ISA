package isa.project.service.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.model.users.User;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.hotel.SingleRoomReservationRepository;
import isa.project.repository.users.UserRepository;

@Service
public class RoomService {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomReservationRepository roomReservationRepository;

	@Autowired
	private SingleRoomReservationRepository singleRoomReservationRepository;

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
	public RoomReservation addReservation(Integer hotelId, String customer, RoomReservationDTO roomReservationDTO)
			throws ResourceNotFoundException, RequestDataException {
		if (roomReservationDTO.getCheckInDate().compareTo(roomReservationDTO.getCheckOutDate()) > 0) {
			throw new RequestDataException("Check in date can not be after check out date.");
		}

		Optional<Hotel> hotel = hotelRepository.findById(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel is not found");
		}

		Optional<User> user = userRepository.findByEmail(customer);

		if (!user.isPresent()) {
			throw new ResourceNotFoundException(customer, "Customer is not found");
		}

		RoomReservation roomReservation = new RoomReservation(roomReservationDTO.getCheckInDate(),
				roomReservationDTO.getCheckOutDate());

		for (AdditionalService a : roomReservationDTO.getAdditionalServices()) {
			Optional<AdditionalService> temp = hotel.get().getAdditionalServices().stream()
					.filter(o -> o.getId().equals(a.getId())).findFirst();

			if (!temp.isPresent()) {
				throw new ResourceNotFoundException(temp.get().getId().toString(),
						"Additional service is not found in that hotel.");
			}

			roomReservation.addAdditionalService(temp.get());
		}

		for (Room r : roomReservationDTO.getReservations()) {
			Optional<Room> room = hotel.get().getRooms().stream().filter(o -> o.getId().equals(r.getId())).findFirst();

			if (!room.isPresent()) {
				throw new ResourceNotFoundException(room.get().getId().toString(), "Room is not found in that hotel.");
			}

			boolean free = true;

			for (SingleRoomReservation srr : room.get().getSingleRoomReservations()) {
				if (roomReservationDTO.getCheckInDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
						&& roomReservationDTO.getCheckInDate()
								.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
				if (roomReservationDTO.getCheckOutDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
						&& roomReservationDTO.getCheckOutDate()
								.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
			}
			if (!free) {
				throw new RequestDataException("Room is booked in that period.");
			}

			SingleRoomReservation newSingleRoomReservation = new SingleRoomReservation(room.get(), roomReservation);
			roomReservation.addSingleRoomReservation(singleRoomReservationRepository.save(newSingleRoomReservation));
		}

		return roomReservationRepository.save(roomReservation);
	}

}
