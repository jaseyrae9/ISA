package isa.project.dto.aircompany;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import isa.project.model.aircompany.Destination;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO za prenos destinacije aviokompanije.
 * 
 * @author Milica
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class DestinationDTO {
	private Long id;
	
	@NotBlank(message = "Destination label can not be blank.")
	@Size(max=3, min = 3)
	private String label;

	@NotBlank(message = "Airport name can not be blank.")
	private String airportName;

	@NotBlank(message = "Country can not be blank.")
	private String country;
	
	@NotBlank(message = "City can not be blank.")
	private String city;	

	public DestinationDTO(Destination destination) {
		this.id = destination.getId();
		this.label = destination.getLabel();
		this.airportName = destination.getAirportName();
		this.country = destination.getCountry();
		this.city = destination.getCity();
	}
}
