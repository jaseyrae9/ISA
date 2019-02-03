package isa.project.dto.aircompany;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.aircompany.Seat.SeatClass;

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
	
	public Double getEconomyPrice() {
		return economyPrice;
	}

	public void setEconomyPrice(Double economyPrice) {
		this.economyPrice = economyPrice;
	}

	public Double getPremiumEconomyPrice() {
		return premiumEconomyPrice;
	}

	public void setPremiumEconomyPrice(Double premiumEconomyPrice) {
		this.premiumEconomyPrice = premiumEconomyPrice;
	}

	public Double getBussinessPrice() {
		return bussinessPrice;
	}

	public void setBussinessPrice(Double bussinessPrice) {
		this.bussinessPrice = bussinessPrice;
	}

	public Double getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
	
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
