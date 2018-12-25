package isa.project.dto.users;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import isa.project.model.users.Customer;

public class CustomerDTO {
private Integer id;
	
	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@NotEmpty
	private String firstName;
	
	@NotNull
	@NotEmpty
	private String lastName;
	
	@NotNull
	@NotEmpty
	private String email;
	
	private String password;
	
	private String matchingPassword;
	
	private String phoneNumber;
	
	@NotNull
	@NotEmpty
	private String address;
	
	private Boolean confirmedMail;
	
	public CustomerDTO() {
		
	}

	public CustomerDTO(Customer customer) {
		this.id = customer.getId();
		this.username = customer.getUsername();
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.email = customer.getEmail();
		this.phoneNumber = customer.getPhoneNumber();
		this.address = customer.getAddress();
		this.confirmedMail = customer.getConfirmedMail();
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

	public Boolean getConfirmedMail() {
		return confirmedMail;
	}

	public void setConfirmedMail(Boolean confirmedMail) {
		this.confirmedMail = confirmedMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}
}
