package isa.project.model.users;

import java.util.ArrayList;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CUST")
public class Customer extends User {
	private static final long serialVersionUID = -3593336673218968856L;

	public Customer() {
		super();
	}
	
	public Customer(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
		super.authorities = new ArrayList<Authority>();
	}
}
