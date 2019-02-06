package isa.project.dto.rentacar;

import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.CarReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CarReservationDTO {

	private Integer id;	
	
	@FutureOrPresent(message = "Day can not be in the past.")
	@NotNull (message = "Pick up date can not be blank.")
	private Date pickUpDate;	
	
	@FutureOrPresent(message = "Day can not be in the past.")
	@NotNull (message = "Drop off date can not be blank.")
	private Date dropOffDate;	
	
	@NotBlank (message = "Pick up location can not be blank.")
	private BranchOffice pickUpBranchOffice;
	
	@NotBlank (message = "Drop off location can not be blank.")
	private BranchOffice dropOffBranchOffice;	
	
	private Boolean active;	
	
	private Boolean isCarRated;	
	
	private Boolean isCompanyRated;	
	
	private CarDTO car;
	
	private Double total;
	
	public CarReservationDTO(CarReservation reservation) {
		this.id = reservation.getId();
		this.pickUpDate = reservation.getPickUpDate();
		this.dropOffDate = reservation.getDropOffDate();
		this.pickUpBranchOffice = reservation.getPickUpBranchOffice();
		this.dropOffBranchOffice = reservation.getDropOffBranchOffice();
		this.active = reservation.getActive();
		this.isCarRated = reservation.getIsCarRated();
		this.isCompanyRated = reservation.getIsCompanyRated();	
		
	}	
}
