package isa.project.service.hotel;

import java.util.Optional;

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
import isa.project.model.users.Customer;
import isa.project.model.users.User;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.hotel.SingleRoomReservationRepository;
import isa.project.repository.shared.AdditionalServiceRepository;
import isa.project.repository.users.UserRepository;

@Service
public class RoomService {

	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomReservationRepository roomReservationRepository;

	@Autowired
	private AdditionalServiceRepository additionalServiceRepository;

	@Autowired
	private SingleRoomReservationRepository singleRoomReservationRepository;

	public RoomReservation addReservation(Integer hotelId, String customer, RoomReservationDTO roomReservationDTO)
			throws ResourceNotFoundException, RequestDataException {
		if(roomReservationDTO.getCheckInDate().compareTo(roomReservationDTO.getCheckOutDate()) > 0)
		{
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

		RoomReservation roomReservation = new RoomReservation((Customer) user.get(),
				roomReservationDTO.getCheckInDate(), roomReservationDTO.getCheckOutDate());

		for (AdditionalService a : roomReservationDTO.getAdditionalServices()) {
			Optional<AdditionalService> temp = additionalServiceRepository.findById(a.getId());

			if (!temp.isPresent()) {
				throw new ResourceNotFoundException(a.getId().toString(), "Additional service " + a.getName() + " is not found");
			}
			
			if (!hotel.get().getAdditionalServices().stream().filter(o -> o.getId().equals(temp.get().getId())).findFirst()
					.isPresent()) {
				throw new ResourceNotFoundException(temp.get().getId().toString(), "Additional service is not found in that hotel.");
			}
			
			roomReservation.addAdditionalService(temp.get());
		}

		for (Room r : roomReservationDTO.getReservations()) {
			Optional<Room> room = roomRepository.findById(r.getId());

			if (!room.isPresent()) {
				throw new ResourceNotFoundException(r.getId().toString(), "Room not found");
			}
			
			if (!hotel.get().getRooms().stream().filter(o -> o.getId().equals(room.get().getId())).findFirst()
					.isPresent()) {
				throw new ResourceNotFoundException(room.get().getId().toString(), "Room is not found in that hotel.");
			}

			boolean free = true;

			for (SingleRoomReservation srr : room.get().getSingleRoomReservations()) {
				if (roomReservationDTO.getCheckInDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0 
						&& roomReservationDTO.getCheckInDate().compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
				if (roomReservationDTO.getCheckOutDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0 
						&& roomReservationDTO.getCheckOutDate().compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
			}
			if(!free) {
				throw new RequestDataException("Room is booked in that period.");
			}
			
			
			SingleRoomReservation newSingleRoomReservation = new SingleRoomReservation(room.get(), roomReservation);
			roomReservation.addSingleRoomReservation(singleRoomReservationRepository.save(newSingleRoomReservation));
		}
		
		
		return roomReservationRepository.save(roomReservation);
	}

}
