package isa.project.dto.aircompany;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;

public class AirCompanyDTO {
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;	
	
	private String description;
	
	private ArrayList<DestinationDTO> destinations;
	
	public AirCompanyDTO() {
		
	}
	
	public AirCompanyDTO(AirCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.description = company.getDescription();
		this.destinations = new ArrayList<>();
		for(Destination destination:company.getDestinations()) {
			this.destinations.add(new DestinationDTO(destination));
		}
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

	public ArrayList<DestinationDTO> getDestinations() {
		return destinations;
	}

	public void setDestinations(ArrayList<DestinationDTO> destinations) {
		this.destinations = destinations;
	}

}
