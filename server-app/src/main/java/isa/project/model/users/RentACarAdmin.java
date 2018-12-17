package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("RACA")
public class RentACarAdmin extends User {

	public RentACarAdmin() {
		super();
	}
}
