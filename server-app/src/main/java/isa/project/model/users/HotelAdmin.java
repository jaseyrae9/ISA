package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("HA")
public class HotelAdmin extends User {
	private static final long serialVersionUID = -720564991774592571L;

	public HotelAdmin() {
		super();
	}
}
