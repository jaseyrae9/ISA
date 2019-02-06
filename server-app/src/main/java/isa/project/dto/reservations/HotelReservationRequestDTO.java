package isa.project.dto.reservations;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HotelReservationRequestDTO {
	@NotNull (message = "Hotel can not be blank.")
	private Integer hotelId;
		
	@FutureOrPresent(message = "Check in day can not be in the past.")
	@NotNull(message = "Check in date can not be empty.")
	private Date checkInDate;
	
	@FutureOrPresent(message = "Check out day can not be in the past.")
	@NotNull(message = "Check out date can not be empty.")
	private Date checkOutDate;
	
	@NotEmpty (message = "There must be at least one room.")
	private List<Integer> rooms;
	
	private List<Integer> additionalServices;
	
	@NotNull(message = "Please tell what you want.")
	private Boolean isFastReservation;
}
