package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.aircompany.AirCompany;

@Entity
@DiscriminatorValue("ACA")
public class AirCompanyAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "air_company_id")
	private AirCompany airCompany;

	public AirCompanyAdmin() {
		super();
	}

	/**
	 * Create air company admin based on UserDTO object. Used when new hotel air company admin is being
	 * registered.
	 * @param userDTO
	 */
	public AirCompanyAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
	}

	public AirCompany getAirCompany() {
		return airCompany;
	}

	public void setAirCompany(AirCompany avioCompany) {
		this.airCompany = avioCompany;
	}

}
