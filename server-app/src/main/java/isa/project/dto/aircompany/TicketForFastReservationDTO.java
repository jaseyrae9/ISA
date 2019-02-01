package isa.project.dto.aircompany;

import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.TicketForFastReservation;

public class TicketForFastReservationDTO {
	private Integer id;
	private Flight flight;
	private Ticket ticket;

	public TicketForFastReservationDTO(TicketForFastReservation ticket) {
		this.id = ticket.getId();
		this.flight = ticket.getTicket().getFlight();
		this.ticket = ticket.getTicket();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
}
