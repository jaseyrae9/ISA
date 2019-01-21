package isa.project.dto.aircompany;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.aircompany.Seat;
import isa.project.model.aircompany.Seat.SeatClass;

public class SeatDTO {
	private Integer id;
	
	@NotNull(message = "Seat class can not be empty.")
	private SeatClass seatClass;
	
	@NotNull(message = "Row number can not be blank.")
	@Min(value = 1, message = "Minimal row number is 1.")
	private Integer rowNum;
	
	@NotNull(message = "Column number can not be blank.")
	@Min(value = 1, message = "Minimal column number is 1.")
	private Integer colNum;
	
	public SeatDTO() {
		
	}
	
	public SeatDTO(Seat seat) {
		this.id = seat.getId();
		this.seatClass = seat.getSeatClass();
		this.rowNum= seat.getRowNum();
		this.colNum = seat.getColNum();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SeatClass getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNumber) {
		this.rowNum = rowNumber;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNumber) {
		this.colNum = colNumber;
	}
}
