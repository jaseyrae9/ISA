package isa.project.dto.aircompany;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import isa.project.model.aircompany.Airplane;
import isa.project.model.aircompany.Airplane.AirplaneStatus;
import isa.project.model.aircompany.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AirplaneDTO {
	@Getter
	@Setter
	private Integer id;
	
	@Getter
	@Setter
	@NotBlank(message = "Airplane name can not be blank.")
	private String name;
	
	@Getter
	@Setter
	private AirplaneStatus status;
	
	@Getter
	@Setter
	@NotNull(message = "Row number can not be blank.")
	@Min(value = 1, message = "There must be at least one row.")
	private Integer rowNum;

	@Getter
	@Setter
	@NotNull(message = "Column number can not be blank.")
	@Min(value = 1, message = "There must be at least one column.")
	private Integer colNum;	

	@Getter
	@Setter
	@NotNull(message = "Seats per column number can not be blank.")
	@Min(value = 1, message = "There must be at least one seat per column.")
	private Integer seatsPerCol;
	
	@NotEmpty(message = "There must be at least one seat.")
	private List<SeatDTO> seats;
	
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

	public ArrayList<List<SeatDTO>> getSeats() {
		ArrayList<List<SeatDTO>> ret = new ArrayList<>();
		int itemsInRow = this.colNum * this.seatsPerCol;
		int lower = 0;
		for(int i = 0; i < this.rowNum; ++i) {
			List<SeatDTO> row = seats.subList(lower, lower + itemsInRow);
			lower += itemsInRow;
			ret.add(row);
		}
		return ret;
	}
	
	@JsonIgnore
	public List<SeatDTO> getSeatsAsArray() {
		return seats;
	}

	public void setSeats(ArrayList<SeatDTO> seats){
		this.seats = seats;
	}	
}
