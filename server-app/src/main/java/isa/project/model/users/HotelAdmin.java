package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("HA")
public class HotelAdmin extends User {

	public HotelAdmin() {
		super();
	}
}
