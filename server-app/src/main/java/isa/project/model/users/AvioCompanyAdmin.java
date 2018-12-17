package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("ACA")
public class AvioCompanyAdmin extends User {

	public AvioCompanyAdmin() {
		
	}
}
