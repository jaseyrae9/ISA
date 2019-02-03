package isa.project.service.hotel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.hotel.SingleRoomReservationRepository;

@Service
public class RoomReservationService {

	@Autowired
	private RoomReservationRepository roomReservationReposiory;
	
	@Autowired
	private SingleRoomReservationRepository singleRoomReservationRepository;
	
	public Optional<RoomReservation> findRoomReservation(Integer id) {
		return roomReservationReposiory.findById(id);
	}
	
	public RoomReservation saveRoomReservation(RoomReservation roomReservation) {
		return roomReservationReposiory.save(roomReservation);
	}
	
	public Optional<SingleRoomReservation> findSingleRoomReservation(Integer id) {
		return singleRoomReservationRepository.findById(id);
	}
	
	public SingleRoomReservation saveSingleRoomResrvation(SingleRoomReservation srr) {
		return singleRoomReservationRepository.save(srr);
	}
}
