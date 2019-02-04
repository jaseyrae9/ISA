package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.aircompany.AirCompany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("ACA")
public class AirCompanyAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "air_company_id")
	private AirCompany airCompany;	

	public AirCompanyAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
	}
}
