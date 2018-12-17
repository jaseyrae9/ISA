package isa.project.controller.hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.hotel.HotelDTO;
import isa.project.model.hotel.Hotel;
import isa.project.service.hotel.HotelService;

@RestController
@RequestMapping(value="server/hotels")
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
	 * Adds new hotels.
	 * @param hotel
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO){
		Hotel hotel = new Hotel(hotelDTO.getName(), hotelDTO.getDescription());
		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(hotel)), HttpStatus.CREATED);	
	}
	
	/**
	 * Edit existing hotel.
	 * @param hotel
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<HotelDTO> editHotel(@RequestBody HotelDTO hotelDTO){
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(hotelDTO.getId());
		
		//hotel is not found
		if( opt.isPresent() == false) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//set name and description
		opt.ifPresent( hotel -> {
			hotel.setName(hotel.getName());
			hotel.setDescription(hotel.getDescription());
		});
		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(opt.get())), HttpStatus.OK);	
	}
	
}
