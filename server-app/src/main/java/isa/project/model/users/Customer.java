package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("CUST")
public class Customer extends User{

	public Customer() {
		super();
	}

	public Customer(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
	}
}
