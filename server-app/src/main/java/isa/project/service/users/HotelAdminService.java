package isa.project.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import isa.project.model.users.HotelAdmin;
import isa.project.repository.users.HotelAdminRepository;

@Service
public class HotelAdminService {
	
	@Autowired
	private HotelAdminRepository hotelAdminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/* For registration */
	public HotelAdmin registerHotelAdmin(HotelAdmin hotelAdmin)
	{		
		hotelAdmin.setPassword(passwordEncoder.encode(hotelAdmin.getPassword()));
		return hotelAdminRepository.save(hotelAdmin);
	}
	
	public Iterable<HotelAdmin> findAll(){
		return hotelAdminRepository.findAll();
	}
	
	public Optional<HotelAdmin> findCustomer(Integer id){
		return hotelAdminRepository.findById(id);
	}
	
	public HotelAdmin saveCustomer(HotelAdmin admin) {
		return hotelAdminRepository.save(admin);
	}
}
