package isa.project.dto.users;

import isa.project.model.users.RentACarAdmin;

public class RentACarAdminDTO {
	private Integer id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String address;
	private Boolean confirmedMail;

	public RentACarAdminDTO() {
		
	}
	
	public RentACarAdminDTO(RentACarAdmin admin) {
		this.id = admin.getId();
		this.username = admin.getUsername();
		this.password = admin.getPassword();
		this.firstName = admin.getFirstName();
		this.lastName = admin.getLastName();
		this.email = admin.getEmail();
		this.phoneNumber = admin.getPhoneNumber();
		this.address = admin.getAddress();
		this.confirmedMail = admin.getConfirmedMail();
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

	public Boolean getConfirmedMail() {
		return confirmedMail;
	}

	public void setConfirmedMail(Boolean confirmedMail) {
		this.confirmedMail = confirmedMail;
	}
	
	
}
