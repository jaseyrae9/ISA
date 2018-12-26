package isa.project.dto.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.users.User;

/**
 * Class used as DTO when new customer is being registered.
 *
 */
public class UserProfileDTO {
	private Integer id;	
	
	@NotNull (message = "Username must be entered.")
	@NotBlank (message = "Username can not be blank.")
	private String username;
	
	@NotNull (message = "Password must be entered.")
	@NotBlank (message = "Password can not be blank.")
	private String password;

	@NotNull (message = "First name must be entered.")
	@NotBlank (message = "First name can not be blank.")
	private String firstName;

	@NotNull (message = "Last name must be entered.")
	@NotBlank (message = "Last name can not be blank.")
	private String lastName;	

	@NotNull (message = "Email must be entered.")
	@NotBlank (message = "Email can not be blank.")
	@Email (message = "Email does not match format.")
	private String email;	

	@NotNull (message = "Phone number must be entered.")
	@NotBlank (message = "Phone number can not be blank.")
	private String phoneNumber;

	@NotNull (message = "Address must be entered.")
	@NotBlank (message = "Address can not be blank.")
	private String address;
	
	public UserProfileDTO() {
		
	}

	public UserProfileDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
