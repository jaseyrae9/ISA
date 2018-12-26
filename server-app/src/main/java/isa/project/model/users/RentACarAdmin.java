package isa.project.model.users;

import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.model.rentacar.RentACarCompany;

@Entity
@DiscriminatorValue("RACA")
public class RentACarAdmin extends User { 

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rent_a_car_company_id")
	private RentACarCompany rentACarCompany;
	
	public RentACarAdmin() {
		super();
	}
	
	public RentACarAdmin(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}

	public RentACarCompany getRentACarCompany() {
		return rentACarCompany;
	}

	public void setRentACarCompany(RentACarCompany rentACarCompany) {
		this.rentACarCompany = rentACarCompany;
	}
	
	
}
