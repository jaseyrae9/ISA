package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.hotel.Hotel;

@Entity
@DiscriminatorValue("HA")
public class HotelAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;

	public HotelAdmin() {
		super();
	}

	/**
	 * Create hotel admin based on UserDTO object. Used when new hotel admin is being
	 * registered.
	 * @param userDTO
	 */
	public HotelAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
}
