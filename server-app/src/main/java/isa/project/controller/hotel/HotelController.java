package isa.project.controller.hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.HotelAdminCheck;
import isa.project.aspects.RentACarCompanyAdminCheck;
import isa.project.dto.hotel.HotelDTO;
import isa.project.dto.hotel.RoomDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.hotel.HotelService;

@RestController
@RequestMapping(value="hotels")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	/**
	 * Returns DTO objects for hotels. Objects contain id, name, address and description.
	 * @return information about all hotels.
	 */
	@RequestMapping(value="/all", method = RequestMethod.GET)
	public ResponseEntity<List<HotelDTO>> getAllHotels(){
		Iterable<Hotel> hotels = hotelService.findAll();
		
		//convert hotels to DTO
		List<HotelDTO> ret = new ArrayList<>();
		for(Hotel hotel:hotels) {
			ret.add(new HotelDTO(hotel));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	
	/**
	 * Returns data about hotel with selected id.
	 * 
	 * @param id - id of hotel
	 * @return
	 * @throws ResourceNotFoundException - if there is no hotel with selected id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCompany(@PathVariable Integer id) throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(id);
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		return new ResponseEntity<>(hotel.get(), HttpStatus.OK);
	}
	
	/**
	 * Adds new hotels.
	 * @param hotel
	 * @return
	 */
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<HotelDTO> addHotel(@Valid @RequestBody HotelDTO hotelDTO){
		System.out.println("Kreira se novi hotel"  + hotelDTO.getName()) ;
		Hotel hotel = new Hotel(hotelDTO.getName(), hotelDTO.getDescription());
		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(hotel)), HttpStatus.CREATED);	
	}
	
	/**
	 * Edit existing hotel.
	 * @param hotel
	 * @return
	 * @throws ResourceNotFoundException 
	 */
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/edit/{id}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<HotelDTO> editHotel(@PathVariable Integer id, @RequestBody HotelDTO hotelDTO) throws ResourceNotFoundException{
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(hotelDTO.getId());
		
		//hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(hotelDTO.getId().toString(), "Hotel not found");
		}
		
		//set name and description
		opt.ifPresent( hotel -> {
			System.out.println("New name is: " + hotelDTO.getName());
			hotel.setName(hotelDTO.getName());
			hotel.setDescription(hotelDTO.getDescription());
		});
		
		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(opt.get())), HttpStatus.OK);	
	}
	
	/**
	 * Adds new room to hotel. 
	 * @param car
	 * @return
	 */	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/addRoom/{hotelId}",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<RoomDTO> addRoom(@PathVariable Integer hotelId, @RequestBody RoomDTO roomDTO) throws ResourceNotFoundException{
		Room room = new Room(roomDTO.getFloor(), roomDTO.getRoomNumber(), roomDTO.getNumberOfBeds(), roomDTO.getPrice(), roomDTO.getType());
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		hotel.get().getRooms().add(room);
		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(new RoomDTO(room), HttpStatus.CREATED);	
	}
	
	/**
	 * Edit room of hotel. 
	 * @param room
	 * @return
	 */
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/editRoom/{hotelId}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<RoomDTO> editRoom(@PathVariable Integer hotelId, @RequestBody RoomDTO roomDTO) throws ResourceNotFoundException{
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		for(Room r : hotel.get().getRooms())
		{
			if(r.getId().equals(roomDTO.getId()))
			{
				r.setRoomNumber(roomDTO.getRoomNumber());
				r.setFloor(roomDTO.getFloor());
				r.setNumberOfBeds(roomDTO.getNumberOfBeds());
				r.setPrice(roomDTO.getPrice());
				r.setType(roomDTO.getType());
			}
		}
				
		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(roomDTO, HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/deleteRoom/{hotelId}/{roomId}",method=RequestMethod.DELETE, consumes="application/json")
	public ResponseEntity<?> deleteRoom(@PathVariable Integer hotelId, @PathVariable Integer roomId) throws ResourceNotFoundException{
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		for(Room r : hotel.get().getRooms())
		{
			if(r.getId().equals(roomId))
			{
				r.setActive(false);
				break;
			}
		}
				
		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(roomId, HttpStatus.OK);	
	}
}
