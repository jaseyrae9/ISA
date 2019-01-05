package isa.project.controller.hotel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.hotel.HotelDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.hotel.Hotel;
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
	@RequestMapping(value="/edit",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<HotelDTO> editHotel(@RequestBody HotelDTO hotelDTO) throws ResourceNotFoundException{
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
	
}
