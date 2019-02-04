package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.rentacar.RentACarCompany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("RACA")
public class RentACarAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "rent_a_car_company_id")
	private RentACarCompany rentACarCompany;

	public RentACarAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
	}
}
