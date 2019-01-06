package isa.project.dto.aircompany;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import isa.project.model.aircompany.Destination;

/**
 * DTO za prenos destinacije aviokompanije.
 * 
 * @author Milica
 *
 */
public class DestinationDTO {
	private Long id;
	
	@NotBlank(message = "Destination label can not be blank.")
	@Size(max=3, min = 3)
	private String label;

	@NotBlank(message = "Airport name can not be blank.")
	private String airportName;

	@NotBlank(message = "Country can not be blank.")
	private String country;
	
	public DestinationDTO() {
		
	}

	public DestinationDTO(Destination destination) {
		this.id = destination.getId();
		this.label = destination.getLabel();
		this.airportName = destination.getAirportName();
		this.country = destination.getCountry();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
