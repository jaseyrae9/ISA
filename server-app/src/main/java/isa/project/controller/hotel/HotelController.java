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
import isa.project.dto.hotel.HotelDTO;
import isa.project.dto.hotel.RoomDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.shared.AdditionalService;
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
		Room room = hotelService.addRoom(hotelId, roomDTO);
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
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/addAdditionalService/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addBaggageInformation(@PathVariable Integer id, @Valid @RequestBody AdditionalService additionalService) throws ResourceNotFoundException {
		AdditionalService service = hotelService.addAdditionalService(additionalService, id);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/deleteAdditionalService/{hotelId}/{serviceId}",method=RequestMethod.DELETE, consumes="application/json")
	public ResponseEntity<?> deleteService(@PathVariable Integer hotelId, @PathVariable Long serviceId) throws ResourceNotFoundException{
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		for(AdditionalService as : hotel.get().getAdditionalServices())
		{
			if(as.getId().equals(serviceId))
			{
				as.setActive(false);
				break;
			}
		}
				
		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(serviceId, HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/editAdditionalService/{hotelId}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> editService(@PathVariable Integer hotelId, @Valid @RequestBody AdditionalService additionalService) throws ResourceNotFoundException{
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		for(AdditionalService as : hotel.get().getAdditionalServices())
		{
			if(as.getId().equals(additionalService.getId()))
			{
				as.setName(additionalService.getName());
				as.setDescription(additionalService.getDescription());
				as.setPrice(additionalService.getPrice());
				break;
			}
		}
				
		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(additionalService, HttpStatus.OK);	
	}
	
}
