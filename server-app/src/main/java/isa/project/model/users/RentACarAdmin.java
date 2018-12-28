package isa.project.model.users;

import java.util.HashSet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.dto.users.UserDTO;
import isa.project.model.rentacar.RentACarCompany;

@Entity
@DiscriminatorValue("RACA")
public class RentACarAdmin extends User {

	@ManyToOne
	@JoinColumn(name = "rent_a_car_company_id")
	private RentACarCompany rentACarCompany;

	public RentACarAdmin() {
		super();
	}

	public RentACarAdmin(String email, String password, String firstName, String lastName, String phoneNumber,
			String address) {
		super(email, password, firstName, lastName, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}
	
	/**
	 * Create rent a car admin based on UserDTO object. Used when new hotel rent a car admin is being
	 * registered.
	 * @param userDTO
	 */
	public RentACarAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress());
	}

	public RentACarCompany getRentACarCompany() {
		return rentACarCompany;
	}

	public void setRentACarCompany(RentACarCompany rentACarCompany) {
		this.rentACarCompany = rentACarCompany;
	}

}
