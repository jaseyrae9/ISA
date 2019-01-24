package isa.project.dto.rentacar;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.shared.Location;

public class BranchOfficeDTO {

	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	
	private Boolean active;
	
	@NotNull (message = "Location must be enetered.")
	private Location location;
	
	public BranchOfficeDTO() {
		
	}
	
	public BranchOfficeDTO(BranchOffice branchOffice) {
		this.id = branchOffice.getId();
		this.name = branchOffice.getName();
		this.active = branchOffice.getActive();
		this.location = branchOffice.getLocation();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
