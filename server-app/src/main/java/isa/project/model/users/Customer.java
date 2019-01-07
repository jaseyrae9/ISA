package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import isa.project.dto.users.UserDTO;

@Entity
@DiscriminatorValue("CUST")
public class Customer extends User {

	public Customer() {
		super();
	}

	/**
	 * Create customer based on UserDTO object. Used when new customer is being
	 * registered.
	 * 
	 * @param userDTO
	 */
	public Customer(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), false);
	}
}
