package isa.project.dto.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import isa.project.model.users.User;

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

	public UserDTO() {

	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.confirmedMail = user.getConfirmedMail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getConfirmedMail() {
		return confirmedMail;
	}

	public void setConfirmedMail(Boolean confirmedMail) {
		this.confirmedMail = confirmedMail;
	}
}
