package isa.project.model.users;

import java.util.HashSet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.hotel.Hotel;
import isa.project.model.users.security.Authority;

@Entity
@DiscriminatorValue("HA")
public class HotelAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;

	public HotelAdmin() {
		super();
	}

	public HotelAdmin(String email, String password, String firstName, String lastName, String phoneNumber,
			String address) {
		super(email, password, firstName, lastName, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}
	
	/**
	 * Create hotel admin based on UserDTO object. Used when new hotel admin is being
	 * registered.
	 * @param userDTO
	 */
	public HotelAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress());
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
}
