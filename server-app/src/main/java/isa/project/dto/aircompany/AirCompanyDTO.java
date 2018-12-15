package isa.project.dto.aircompany;

import isa.project.model.aircompany.AirCompany;

public class AirCompanyDTO {
	private Integer id;
	private String name;
	private String description;
	
	public AirCompanyDTO() {
		
	}
	
	public AirCompanyDTO(AirCompany company) {
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
