package isa.project.model.users;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;

@Entity
@Table(name="users")
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=STRING)
public abstract class User {

	@Id
	@Column(name="username", unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String username;
	
	@Column(name="password", unique = false, nullable = false)
	private String password;
	
	@Column(name="firstName", unique = false, nullable = false)
	private String firstName;
	
	@Column(name="lastName", unique = false, nullable = false)
	private String lastName;
	
	@Column(name="email", unique = true, nullable = false, columnDefinition="VARCHAR(64)")
	private String email;
	
	@Column(name="phoneNumber", unique = true, nullable = true,  columnDefinition="VARCHAR(64)")
	private String phoneNumber;
	
	@Column(name="address", unique = false, nullable = false)
	private String address;
	
	@Column(name="confirmedMail", nullable = false)
	private Boolean confirmedMail;
	
	public User() {
		super();
	}
	
	public User(String username, String password, String firstName, String lastName, String email, String phoneNumber,
			String address) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.confirmedMail = false; //when user is created, email is not confirmed
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

	@Override
	public int hashCode() {
		return Objects.hashCode(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
//		if (confirmedMail == null) {
//			if (other.confirmedMail != null)
//				return false;
//		} else if (!confirmedMail.equals(other.confirmedMail))
//			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
}
