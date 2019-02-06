package isa.project.dto.aircompany;

import java.sql.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class FlightSearchDTO {
	@NotBlank(message = "Departure airport is required.")
	private String departureAirport;
	@NotBlank(message = "Arrival airport is required.")
	private String arrivalAirport;
	@NotNull(message = "Arrival time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date start;
	@NotNull(message = "Arrival time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date end;
	@NotNull(message = "Number of tickets required can not be blank.")
	@Min(value = 0, message = "Number of tickets required can not be less than 0.")
	private Long numberOfPeople;
	@Min(value = 0, message = "Minimal price can not be less than 0.")
	private Double minPrice = 0.0;
	@Min(value = 0, message = "Maximal price can not be less than 0.")
	private Double maxPrice= Double.MAX_VALUE;
	
	public void setMinPrice(Double minPrice) {
		if(minPrice != null) {
			this.minPrice = minPrice;
		}
	}
	
	public void setMaxPrice(Double maxPrice) {
		if(maxPrice != null) {
			this.maxPrice = maxPrice;
		}
	}
}
