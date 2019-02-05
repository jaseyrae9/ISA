package isa.project.dto.reservations;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CarReservationRequestDTO {
	@NotNull (message = "Car can not be blank.")
	private Integer carId;
	
	@NotNull (message = "Pick up branch office can not be blank.")
	private Integer pickUpBranchOffice;
	
	@NotNull (message = "Drop off branch office can not be blank.")
	private Integer dropOffBranchOffice;
	
	@NotNull(message = "Pick up date can not be empty.")
	private Date pickUpDate;
		
	@NotNull(message = "Drop off date can not be empty.")
	private Date dropOffDate;
	
	@NotNull(message = "Please tell what you want.")
	private Boolean isFastReservation;
}
