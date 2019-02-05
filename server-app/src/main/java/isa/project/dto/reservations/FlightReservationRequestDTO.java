package isa.project.dto.reservations;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class FlightReservationRequestDTO {
	@NotNull (message = "Flight can not be blank.")
	private Integer flightId;
	
	@NotEmpty (message = "There must be at least one ticket.")
	@Valid
	private List<TicketReservationRequestDTO> ticketReservations;
}
