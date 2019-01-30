package isa.project.dto.rentacar;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.CarReservation;

public class CarReservationDTO {

	private Integer id;
	
	@NotNull (message = "Pick up date can not be blank.")
	private Date pickUpDate;
	
	@NotNull (message = "Drop off date can not be blank.")
	private Date dropOffDate;
	
	@NotBlank (message = "Pick up location can not be blank.")
	private BranchOffice pickUpBranchOffice;

	@NotBlank (message = "Drop off location can not be blank.")
	private BranchOffice dropOffBranchOffice;
	
	private Boolean active;
	
	private Boolean isRated;
	
	public CarReservationDTO() {
		
	}

	public CarReservationDTO(CarReservation reservation) {
		this.id = reservation.getId();
		this.pickUpDate = reservation.getPickUpDate();
		this.dropOffDate = reservation.getDropOffDate();
		this.pickUpBranchOffice = reservation.getPickUpBranchOffice();
		this.dropOffBranchOffice = reservation.getDropOffBranchOffice();
		this.active = reservation.getActive();
		this.isRated = reservation.getIsRated();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	public Date getDropOffDate() {
		return dropOffDate;
	}

	public void setDropOffDate(Date dropOffDate) {
		this.dropOffDate = dropOffDate;
	}

	public BranchOffice getPickUpBranchOffice() {
		return pickUpBranchOffice;
	}

	public void setPickUpBranchOffice(BranchOffice pickUpBranchOffice) {
		this.pickUpBranchOffice = pickUpBranchOffice;
	}

	public BranchOffice getDropOffBranchOffice() {
		return dropOffBranchOffice;
	}

	public void setDropOffBranchOffice(BranchOffice dropOffBranchOffice) {
		this.dropOffBranchOffice = dropOffBranchOffice;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getIsRated() {
		return isRated;
	}

	public void setIsRated(Boolean isRated) {
		this.isRated = isRated;
	}

}
