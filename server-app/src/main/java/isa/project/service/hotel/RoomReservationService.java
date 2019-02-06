package isa.project.service.hotel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.reservations.HotelReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomReservationRepository;
import isa.project.repository.hotel.SingleRoomReservationRepository;

@Service
public class RoomReservationService {

	@Autowired
	private RoomReservationRepository roomReservationRepository;

	@Autowired
	private SingleRoomReservationRepository singleRoomReservationRepository;
	
	@Autowired
	private HotelRepository hotelRepository;

	public Optional<RoomReservation> findRoomReservation(Integer id) {
		return roomReservationRepository.findById(id);
	}

	public RoomReservation saveRoomReservation(RoomReservation roomReservation) {
		return roomReservationRepository.save(roomReservation);
	}

	public Optional<SingleRoomReservation> findSingleRoomReservation(Integer id) {
		return singleRoomReservationRepository.findById(id);
	}

	public SingleRoomReservation saveSingleRoomResrvation(SingleRoomReservation srr) {
		return singleRoomReservationRepository.save(srr);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RoomReservation reserve(HotelReservationRequestDTO hotelReservationRequest) throws RequestDataException, ResourceNotFoundException {
		System.out.println("CheckInDate: " + hotelReservationRequest.getCheckInDate());
		System.out.println("CheckOutDate: " + hotelReservationRequest.getCheckInDate());

		if (hotelReservationRequest.getCheckInDate().compareTo(hotelReservationRequest.getCheckOutDate()) > 0) {
			throw new RequestDataException("Check in date can not be after check out date.");
		}

		Optional<Hotel> hotel = hotelRepository.findById(hotelReservationRequest.getHotelId());

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelReservationRequest.getHotelId().toString(), "Hotel is not found");
		}


		RoomReservation roomReservation = new RoomReservation(hotelReservationRequest.getCheckInDate(), hotelReservationRequest.getCheckOutDate());

		if(hotelReservationRequest.getIsFastReservation()) {
			// Pronadji predefinisane servise i dodaj ih
			for(AdditionalService as : hotel.get().getAdditionalServices()) {
				if(as.getIsFast()) {
					roomReservation.addAdditionalService(as);
				}
			}
			
		}
		else {
			// Dodajemo servise koji su poslati
			for (Integer id : hotelReservationRequest.getAdditionalServices()) {
				Optional<AdditionalService> temp = hotel.get().getAdditionalServices().stream()
						.filter(o -> o.getId().equals(new Long(id))).findFirst();

				if (!temp.isPresent()) {
					throw new ResourceNotFoundException(id.toString(), "Additional service is not found in that hotel.");
				}

				roomReservation.addAdditionalService(temp.get());
			}
		}
		
		for (Integer room_index : hotelReservationRequest.getRooms()) {
			Optional<Room> room = hotel.get().getRooms().stream().filter(o -> o.getId().equals(room_index)).findFirst();

			if (!room.isPresent()) {
				throw new ResourceNotFoundException(room_index.toString(), "Room is not found in that hotel.");
			}

			boolean free = true;
			
			
			for (SingleRoomReservation srr : room.get().getSingleRoomReservations()) {
				if (hotelReservationRequest.getCheckInDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
						&& hotelReservationRequest.getCheckInDate().compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
				if (hotelReservationRequest.getCheckOutDate().compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
						&& hotelReservationRequest.getCheckOutDate().compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
					free = false;
					break;
				}
			}
			if (!free) {
				throw new RequestDataException("Room is booked in that period.");
			}

			System.out.println(room.get());
			// Proveriti i da se nalazi na brzoj rezervaciji
			if(room.get().getIsFast() && !hotelReservationRequest.getIsFastReservation())
			{
				if (hotelReservationRequest.getCheckInDate().compareTo(room.get().getBeginDate()) >= 0
						&& hotelReservationRequest.getCheckInDate().compareTo(room.get().getEndDate()) <= 0) {
					throw new RequestDataException("Room is on fast reservation in that period.");
				}
				if (hotelReservationRequest.getCheckOutDate().compareTo(room.get().getBeginDate()) >= 0
						&& hotelReservationRequest.getCheckInDate().compareTo(room.get().getEndDate()) <= 0) {
					throw new RequestDataException("Room is on fast reservation in that period.");
				}
			}
			
			
			SingleRoomReservation newSingleRoomReservation = new SingleRoomReservation(room.get(), roomReservation);
			roomReservation.addSingleRoomReservation(newSingleRoomReservation); //singleRoomReservationRepository.save(
		}
		System.out.println("Gotova room rezervacija");
		return roomReservation; //roomReservationRepository
	}
}
