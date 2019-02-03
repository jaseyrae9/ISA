package isa.project.dto.aircompany;

import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.Ticket;
import isa.project.model.aircompany.TicketForFastReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TicketForFastReservationDTO {
	private Integer id;
	private Flight flight;
	private Ticket ticket;

	public TicketForFastReservationDTO(TicketForFastReservation ticket) {
		this.id = ticket.getId();
		this.flight = ticket.getTicket().getFlight();
		this.ticket = ticket.getTicket();
	}

}
