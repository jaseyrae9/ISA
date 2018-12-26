package isa.project.model.users;

import java.util.HashSet;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	public HotelAdmin(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
}
