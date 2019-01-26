package isa.project.service.hotel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.model.users.Customer;
import isa.project.model.users.User;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.hotel.SingleRoomReservationRepository;
import isa.project.repository.shared.AdditionalServiceRepository;
import isa.project.repository.users.UserRepository;

@Service
public class RoomService {

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
	
	public RoomReservation addReservation(String customer, RoomReservationDTO roomReservationDTO)
			throws ResourceNotFoundException {

		Optional<User> user = userRepository.findByEmail(customer);

		if (!user.isPresent()) {
			throw new ResourceNotFoundException(customer, "Customer is not found");
		}

		RoomReservation roomReservation = new RoomReservation((Customer) user.get(),
				roomReservationDTO.getCheckInDate(), roomReservationDTO.getCheckOutDate());

		
		for (AdditionalService a : roomReservationDTO.getAdditionalServices()) {
			Optional<AdditionalService> temp = additionalServiceRepository.findById(a.getId());

			if (!temp.isPresent()) {
				throw new ResourceNotFoundException(customer, "Additional service " + a.getName() + " is not found");
			}
			roomReservation.addAdditionalService(temp.get());
		}
		System.out.println("Dodati dodatni servisi");
		
		for (Room r : roomReservationDTO.getReservations())
		{
			Optional<Room> room = roomRepository.findById(r.getId());
			
			if (!room.isPresent()) {
				throw new ResourceNotFoundException(customer, "Room not found");
			}
			
			SingleRoomReservation newSingleRoomReservation = new SingleRoomReservation(room.get(), roomReservation);
			roomReservation.addSingleRoomReservation(singleRoomReservationRepository.save(newSingleRoomReservation));
		}
		System.out.println("Dodati doda pojedinicne rezerc");

		return roomReservationRepository.save(roomReservation);
	}

}
