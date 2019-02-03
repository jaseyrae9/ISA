package isa.project.dto.aircompany;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateFastReservationTicketsDTO {
	@NotNull(message = "Length can not be empty.")
	@Min(value = 0, message = "Minimal discount is 0.")
	private double discount;
	
	@NotEmpty(message = "Choose at least one ticket to create tickets for fast reservations.")
	private List<Long> tickets;	

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public List<Long> getTickets() {
		return tickets;
	}

	public void setTickets(List<Long> tickets) {
		this.tickets = tickets;
	}
}
