package isa.project.service.hotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.dto.hotel.RoomDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.shared.AdditionalServiceRepository;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private AdditionalServiceRepository additionalServiceRepository;

	public Iterable<Hotel> findAll() {
		return hotelRepository.findAll();
	}
	
	public Page<Hotel> findAll(Pageable page) {
		return hotelRepository.findAll(page);
	}
	
	public Optional<Hotel> findHotel(Integer id) {
		return hotelRepository.findById(id);
	}

	public Hotel saveHotel(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	/**
	 * Dodaje novu dodatnu uslugu u hotel.
	 * 
	 * @param additionalService - informacije o dodatnoj usluzi
	 * @param id                - id hotela kojoj se dodaje dodatna usluga
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe hotel
	 */
	public AdditionalService addAdditionalService(AdditionalService additionalService, Integer id)
			throws ResourceNotFoundException {
		// pronadji hotel u koji se dodaje dodatna usluga
		Optional<Hotel> hotelOpt = hotelRepository.findById(id);
		if (!hotelOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found.");
		}

		Hotel hotel = hotelOpt.get();

		// sacuvaj dodatnu uslugu
		additionalService.setActive(true);
		AdditionalService service = additionalServiceRepository.save(additionalService);

		// sacuvaj informaciju o dodatnoj usluzi u hotelu
		hotel.addAdditionalService(additionalService);
		hotelRepository.save(hotel);

		return service;
	}

	public Room addRoom(Integer hotelId, RoomDTO roomDTO) throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelRepository.findById(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Room room = new Room(hotel.get(), roomDTO.getFloor(), roomDTO.getRoomNumber(), roomDTO.getNumberOfBeds(),
				roomDTO.getPrice(), roomDTO.getType());
		return roomRepository.save(room);
	}

	public Iterable<Hotel> findSearchAll(String hotelName, String hotelAddress, String hotelCheckInDate,
			String hotelCheckOutDate) throws ParseException {

		List<Hotel> ret = new ArrayList<Hotel>();
		Iterable<Hotel> hotels = hotelRepository.findSearchAll(hotelName, hotelAddress);

		for (Hotel hotel : hotels) {
			Hotel hotel_temp = hotelRepository.findById(hotel.getId()).get();
			if (!hotelCheckInDate.equals("") && !hotelCheckOutDate.equals("")) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

				java.util.Date inDate = sdf1.parse(hotelCheckInDate);
				java.sql.Date sqlInDate = new java.sql.Date(inDate.getTime());

				java.util.Date outDate = sdf1.parse(hotelCheckOutDate);
				java.sql.Date sqlOutDate = new java.sql.Date(outDate.getTime());

				boolean free = true;
				outerloop: for (Room r : hotel_temp.getRooms()) {
					for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
						if (sqlInDate.compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
								&& sqlInDate.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
							free = false;
							break outerloop;
						}
						if (sqlOutDate.compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
								&& sqlOutDate.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
							free = false;
							break outerloop;
						}
					}
				}
				if (free) {
					ret.add(hotel_temp);
				}
			}
			else 
			{
				ret.add(hotel_temp);
			}

		}
		return ret;
	}
}
