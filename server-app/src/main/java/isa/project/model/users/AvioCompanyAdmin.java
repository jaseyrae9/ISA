package isa.project.model.users;

import java.util.HashSet;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import isa.project.model.aircompany.AirCompany;

@Entity
@DiscriminatorValue("ACA")
public class AvioCompanyAdmin extends User {
	
	@ManyToOne
	@JoinColumn(name = "air_company_id")
	private AirCompany avioCompany;
	
	public AvioCompanyAdmin() {
		super();
	}
	
	public AvioCompanyAdmin(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String address) {
		super(username, password, firstName, lastName, email, phoneNumber, address);
		super.authorities = new HashSet<Authority>();
	}

	public AirCompany getAvioCompany() {
		return avioCompany;
	}

	public void setAvioCompany(AirCompany avioCompany) {
		this.avioCompany = avioCompany;
	}
	
}
