package isa.project.dto.aircompany;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import isa.project.model.aircompany.Seat.SeatClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Milica
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class FlightDTO {
	@NotNull(message = "Seat class can not be empty.")
	private Integer airplaneId;
		
	@Size(min = 2, message = "There must be at least two validations")
	private List<Long> destinations;
	
	@NotNull(message = "Departure time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date startDateAndTime;
	
	@NotNull(message = "Arrival time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date endDateAndTime;
	
	@NotNull(message = "Length can not be empty.")
	@Min(value = 0, message = "Minimal length is 0.")
	private Double length;
	
	@NotNull(message = "Maximal number of carry on bags can not be empty.")
	@Min(value = 0, message = "Maximal number of carry on bags can not be less than 0.")
	private Integer maxCarryOnBags;
	
	@NotNull(message = "Maximal number od checked bags can not be empty.")
	@Min(value = 0, message = "Maximal number od checked bags can not be less than 0.")
	private Integer maxCheckedBags;
	
	@NotNull(message = "Additional services available can not be blank.")
	private Boolean additionalServicesAvailable;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double economyPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double premiumEconomyPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double bussinessPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double firstPrice;
		
	public Double getPriceForClass(SeatClass seatClass) {
		switch (seatClass) {
		case PREMIUM_ECONOMY:
			return premiumEconomyPrice;
		case FIRST:
			return firstPrice;
		case BUSSINESS:
			return bussinessPrice;
		default:
			return economyPrice;
		}
	}
}
