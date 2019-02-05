package isa.project.dto.reservations;

import javax.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReservationRequestDTO {
	@Valid
	private FlightReservationRequestDTO flightReservationRequest;
	
	@Valid
	private HotelReservationRequestDTO hotelReservationRequest;
	
	@Valid
	private CarReservationRequestDTO carReservationRequest;
}
