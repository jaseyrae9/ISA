package isa.project.dto.aircompany;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.aircompany.Seat.SeatClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FlightTicketsPriceChangeDTO {
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
