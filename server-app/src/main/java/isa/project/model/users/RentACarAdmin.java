package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RACA")
public class RentACarAdmin extends User {
	private static final long serialVersionUID = 4559388228303860457L;

	public RentACarAdmin() {
		super();
	}
}
