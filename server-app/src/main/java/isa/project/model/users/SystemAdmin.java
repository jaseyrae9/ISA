package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import isa.project.dto.users.UserDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("SYS")
public class SystemAdmin extends User{

	public SystemAdmin(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), true);
		this.setConfirmedMail(true);
		this.setNeedsPasswordChange(false);
	}
	
}
