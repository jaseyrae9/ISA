package isa.project.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SYS")
public class SystemAdmin extends User{
	
}
