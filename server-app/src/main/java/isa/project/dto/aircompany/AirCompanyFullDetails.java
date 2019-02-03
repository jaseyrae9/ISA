package isa.project.dto.aircompany;

import java.util.ArrayList;

import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AirCompanyFullDetails {
	private Integer id;
	private String name;
	private String description;
	private Location location;
	private ArrayList<DestinationDTO> destinations = new ArrayList<>();
	private ArrayList<AdditionalService> baggageInformation = new ArrayList<>();

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
}
