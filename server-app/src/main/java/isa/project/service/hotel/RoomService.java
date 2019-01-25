package isa.project.service.hotel;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.User;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.users.UserRepository;

@Service
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomReservationRepository roomReservationRepository;

	public RoomReservation addReservation(String customer, RoomReservationDTO roomReservationDTO) throws ResourceNotFoundException {
		
		Optional<User> user = userRepository.findByEmail(customer);
		
		if (!user.isPresent()) {
			throw new ResourceNotFoundException(customer, "Customer is not found");
		}
		
		//nisam siguran da ce ovo raditi
		RoomReservation roomReservation = new RoomReservation((Customer)user.get(), roomReservationDTO);
		
		return roomReservationRepository.save(roomReservation);
	}
	
	
}
