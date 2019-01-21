package isa.project.dto.aircompany;

import java.util.ArrayList;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import isa.project.exception_handlers.RequestDataException;
import isa.project.model.aircompany.Airplane;
import isa.project.model.aircompany.Airplane.AirplaneStatus;
import isa.project.model.aircompany.Seat;

public class AirplaneDTO {
	private Integer id;
	
	@NotBlank(message = "Airplane name can not be blank.")
	private String name;
	
	private AirplaneStatus status;
	
	@NotNull(message = "Row number can not be blank.")
	@Min(value = 1, message = "There must be at least one row.")
	private Integer rowNum;

	@NotNull(message = "Column number can not be blank.")
	@Min(value = 1, message = "There must be at least one column.")
	private Integer colNum;	

	@NotNull(message = "Seats per column number can not be blank.")
	@Min(value = 1, message = "There must be at least one seat per column.")
	private Integer seatsPerCol;
	
	@NotEmpty(message = "There must be at least one seat.")
	private ArrayList<SeatDTO> seats;
	
	public AirplaneDTO() {
		
	}
	
	public AirplaneDTO(Airplane airplane) {
		this.id = airplane.getId();
		this.status = airplane.getStatus();
		this.name = airplane.getName();
		this.rowNum = airplane.getRowNum();
		this.colNum = airplane.getColNum();
		this.seatsPerCol = airplane.getSeatsPerCol();
		seats = new ArrayList<>();
		for(Seat seat:airplane.getSeats()) {
			seats.add(new SeatDTO(seat));
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AirplaneStatus getStatus() {
		return status;
	}

	public void setStatus(AirplaneStatus status) {
		this.status = status;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public Integer getSeatsPerCol() {
		return seatsPerCol;
	}

	public void setSeatsPerCol(Integer seatsPerCol) {
		this.seatsPerCol = seatsPerCol;
	}

	public ArrayList<SeatDTO> getSeats() {
		return seats;
	}

	public void setSeats(ArrayList<SeatDTO> seats) throws RequestDataException {
		if(seats.size() != this.colNum * this.rowNum * this.seatsPerCol) {
			throw new RequestDataException("Seats number does not match other parameters.");
		}
		this.seats = seats;
	}	
}
