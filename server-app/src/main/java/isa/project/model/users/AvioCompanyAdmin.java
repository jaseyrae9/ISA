package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ACA")
public class AvioCompanyAdmin extends User {
	private static final long serialVersionUID = -280473255190858629L;

	public AvioCompanyAdmin() {
		
	}
}
