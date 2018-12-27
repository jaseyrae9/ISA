package isa.project.model.users;

import java.util.HashSet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.model.aircompany.AirCompany;

@Entity
@DiscriminatorValue("ACA")
public class AirCompanyAdmin extends User {
	
	@ManyToOne
	@JoinColumn(name = "air_company_id")
	private AirCompany airCompany;
	
	public AirCompanyAdmin() {
		super();
	}
	
	public AirCompanyAdmin(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}

	public AirCompany getAirCompany() {
		return airCompany;
	}

	public void setAirCompany(AirCompany avioCompany) {
		this.airCompany = avioCompany;
	}
	
}
