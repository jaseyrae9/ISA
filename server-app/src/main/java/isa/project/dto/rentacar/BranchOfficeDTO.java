package isa.project.dto.rentacar;

import isa.project.model.rentacar.BranchOffice;

public class BranchOfficeDTO {

	private Integer id;
	private String name;
	private Boolean active;
	
	public BranchOfficeDTO() {
		
	}
	
	public BranchOfficeDTO(BranchOffice branchOffice) {
		this.id = branchOffice.getId();
		this.name = branchOffice.getName();
		this.active = branchOffice.getActive();
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
	
}
