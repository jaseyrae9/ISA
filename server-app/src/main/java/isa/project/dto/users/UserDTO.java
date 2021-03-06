package isa.project.dto.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import isa.project.model.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	private Integer id;
	@NotBlank(message = "Email can not be blank.")
	@Email(message = "Email format is incorrect.")
	private String email;

	@NotBlank(message = "Password can not be blank.")
	private String password;

	@NotBlank(message = "First name can not be blank.")
	private String firstName;

	@NotBlank(message = "Last name can not be blank.")
	private String lastName;

	@NotBlank(message = "Phone number can not be blank.")
	private String phoneNumber;

	@NotBlank(message = "Address can not be blank.")
	private String address;

	private Boolean confirmedMail;

	public UserDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.confirmedMail = user.getConfirmedMail();
	}	
}
