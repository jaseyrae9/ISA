package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.hotel.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("HA")
public class HotelAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;

	public HotelAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
	}
}
