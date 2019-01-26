package isa.project.dto.aircompany;

import java.util.ArrayList;

import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;

public class AirCompanyFullDetails {
	private Integer id;
	private String name;
	private String description;
	private Location location;
	private ArrayList<DestinationDTO> destinations = new ArrayList<>();
	private ArrayList<AdditionalService> baggageInformation = new ArrayList<>();
	
	public AirCompanyFullDetails() {
		
	}
	
	public AirCompanyFullDetails(AirCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.location = company.getLocation();
		this.description = company.getDescription();
		if(company.getDestinations() != null) {
			for(Destination destination:company.getDestinations()) {
				this.destinations.add(new DestinationDTO(destination));
			}
		}
		this.baggageInformation.addAll(company.getBaggageInformation());
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<DestinationDTO> getDestinations() {
		return destinations;
	}

	public void setDestinations(ArrayList<DestinationDTO> destinations) {
		this.destinations = destinations;
	}

	public ArrayList<AdditionalService> getBaggageInformation() {
		return baggageInformation;
	}

	public void setBaggageInformation(ArrayList<AdditionalService> baggageInformation) {
		this.baggageInformation = baggageInformation;
	}
}
