package isa.project.service.hotel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.hotel.Hotel;
import isa.project.repository.hotel.HotelRepository;

@Service
public class HotelService {
	
	@Autowired
	private HotelRepository hotelRepository;
	
	public Iterable<Hotel> findAll(){
		return hotelRepository.findAll();
	}
	
	public Optional<Hotel> findHotel(Integer id){
		return hotelRepository.findById(id);
	}
	
	public Hotel saveHotel(Hotel hotel) {
		return hotelRepository.save(hotel);
	}
	
}
