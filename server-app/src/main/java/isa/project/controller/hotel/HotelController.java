package isa.project.controller.hotel;

import java.text.ParseException;
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
import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.service.hotel.HotelService;
import isa.project.service.hotel.RoomService;

@RestController
@RequestMapping(value="hotels")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RoomService roomService;
	
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
		return new ResponseEntity<>(new HotelDTO(hotel.get()), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get/{name}/{address}/{checkInDate}/{checkOutDate}", method = RequestMethod.GET)
	public ResponseEntity<List<HotelDTO>> getCompany(@PathVariable String name, @PathVariable String address, @PathVariable String checkInDate, @PathVariable String checkOutDate) throws ParseException {
		
		String hotelName = "";
		if(name.split("=").length > 1) {
			hotelName = name.split("=")[1];
		}
		System.out.println("hotelName " + hotelName);
		
		String hotelAddress = "";
		if(address.split("=").length > 1)
		{
			hotelAddress = address.split("=")[1];
			
		}
		System.out.println("hotelAddress " + hotelAddress);
		
		String hotelCheckInDate = "";
		if(checkInDate.split("=").length > 1) {
			hotelCheckInDate = checkInDate.split("=")[1];
		}
		System.out.println("hotelCheckInDate " + hotelCheckInDate);
		
		String hotelCheckOutDate = "";
		if(checkOutDate.split("=").length > 1) {
			hotelCheckOutDate = checkOutDate.split("=")[1];
		}
		System.out.println("hotelCheckOutDate " + hotelCheckOutDate);
		
		Iterable<Hotel> hotels = hotelService.findSearchAll(hotelName, hotelAddress, hotelCheckInDate, hotelCheckOutDate);
		
		//convert hotels to DTO
		List<HotelDTO> ret = new ArrayList<>();
		for(Hotel hotel:hotels) {
			ret.add(new HotelDTO(hotel));
		}
		
		return new ResponseEntity<>(ret, HttpStatus.OK);
		
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
		hotel.setLocation(hotelDTO.getLocation());
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
	public ResponseEntity<HotelDTO> editHotel(@PathVariable Integer id, @Valid @RequestBody HotelDTO hotelDTO) throws ResourceNotFoundException{
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
			hotel.getLocation().setAddress(hotelDTO.getLocation().getAddress());
			hotel.getLocation().setLat(hotelDTO.getLocation().getLat());
			hotel.getLocation().setLon(hotelDTO.getLocation().getLon());
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
	public ResponseEntity<RoomDTO> addRoom(@PathVariable Integer hotelId, @Valid @RequestBody RoomDTO roomDTO) throws ResourceNotFoundException{
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
	public ResponseEntity<RoomDTO> editRoom(@PathVariable Integer hotelId, @Valid @RequestBody RoomDTO roomDTO) throws ResourceNotFoundException{
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
	
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value="/rentRoom/{hotelId}/{customer}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RoomReservationDTO> rentRoom(@PathVariable Integer hotelId, @PathVariable String customer, @Valid @RequestBody RoomReservationDTO roomReservationDTO ) throws ResourceNotFoundException, RequestDataException{
		RoomReservation roomReservation = roomService.addReservation(hotelId, customer, roomReservationDTO);
		return new ResponseEntity<>(new RoomReservationDTO(roomReservation), HttpStatus.CREATED);
	}

}
