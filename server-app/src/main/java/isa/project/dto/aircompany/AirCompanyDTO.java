package isa.project.dto.aircompany;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.aircompany.AirCompany;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AirCompanyDTO {
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;	
	
	private String description;
	
	@NotNull (message = "Location must be enetered.")
	@Valid
	private Location location;
	
	private Double averageRating;	
	
	public AirCompanyDTO(AirCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.location = company.getLocation();
		this.description = company.getDescription();	
		this.averageRating = (company.getTotalRating() / (double) company.getRatingCount());
	}
}
