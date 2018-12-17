package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.users.Customer;
import isa.project.model.users.HotelAdmin;
import isa.project.repository.users.HotelAdminRepository;

@Service
public class HotelAdminService {
	
	@Autowired
	private HotelAdminRepository hotelAdminRepository;
	
	public Iterable<HotelAdmin> findAll(){
		return hotelAdminRepository.findAll();
	}
	
	public Optional<HotelAdmin> findCustomer(String username){
		return hotelAdminRepository.findById(username);
	}
	
	public HotelAdmin saveCustomer(HotelAdmin admin) {
		return hotelAdminRepository.save(admin);
	}
}
