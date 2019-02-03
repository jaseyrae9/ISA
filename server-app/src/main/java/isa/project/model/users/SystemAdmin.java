package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import isa.project.dto.users.UserDTO;

@Entity
@DiscriminatorValue("SYS")
public class SystemAdmin extends User{
	
	public SystemAdmin() {
		super();
	}
	
	/**
	 * Create system admin based on UserDTO object. Used when new system admin is being
	 * registered.
	 * @param userDTO
	 */
	public SystemAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
		this.setNeedsPasswordChange(false);
	}
	
}
