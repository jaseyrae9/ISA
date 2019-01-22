package isa.project.dto.rentacar;

import javax.validation.constraints.NotBlank;

import isa.project.model.rentacar.RentACarCompany;

public class RentACarCompanyDTO {
	
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	private String description;
	
	public RentACarCompanyDTO() {
		
	}
	
	public RentACarCompanyDTO(RentACarCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.description = company.getDescription();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
