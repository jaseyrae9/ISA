package isa.project.model.users;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.dto.users.UserDTO;

@Entity
@DiscriminatorValue("CUST")
public class Customer extends User {

	@JsonManagedReference(value = "customer-reservations")
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Reservation> reservations;
	
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

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	
}
