package isa.project.model.users;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.dto.users.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("CUST")
public class Customer extends User {
	
	@Column
	private Double lengthTravelled;

	@OrderColumn(name = "creationDate")
	@JsonManagedReference(value = "customer-reservations")
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Reservation> reservations;	
	
	@JsonManagedReference(value = "customer-invites")
	@OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<FriendInvite> invites;
	
	public Customer(UserDTO userDTO) {
		super(userDTO.getEmail(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(),
				userDTO.getPhoneNumber(), userDTO.getAddress(), false);
		lengthTravelled = 0.0;
	}
}
