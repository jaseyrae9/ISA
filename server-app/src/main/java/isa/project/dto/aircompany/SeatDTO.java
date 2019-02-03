package isa.project.dto.aircompany;

import java.util.HashMap;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.aircompany.Seat;
import isa.project.model.aircompany.Seat.SeatClass;
import lombok.Getter;
import lombok.Setter;

public class SeatDTO {
	private HashMap<String, SeatClass> possibleClasses;

	@Getter
	@Setter
	private Integer id;


	@Getter
	@NotNull(message = "Seat class can not be empty.")
	private SeatClass seatClass;

	@Getter
	@Setter
	@NotNull(message = "Row number can not be blank.")
	@Min(value = 1, message = "Minimal row number is 1.")
	private Integer rowNum;

	@Getter
	@Setter
	@NotNull(message = "Column number can not be blank.")
	@Min(value = 1, message = "Minimal column number is 1.")
	private Integer colNum;

	public SeatDTO() {
		this.possibleClasses = new HashMap<>();
		this.possibleClasses.put("ECONOMY", SeatClass.ECONOMY);
		this.possibleClasses.put("PREMIUM_ECONOMY", SeatClass.PREMIUM_ECONOMY);
		this.possibleClasses.put("BUSSINESS", SeatClass.BUSSINESS);
		this.possibleClasses.put("FIRST", SeatClass.FIRST);
	}

	public SeatDTO(Seat seat) {
		this.id = seat.getId();
		this.seatClass = seat.getSeatClass();
		this.rowNum = seat.getRowNum();
		this.colNum = seat.getColNum();
	}

	public void setSeatClass(String seatClass) {
		if (possibleClasses.containsKey(seatClass)) {
			this.seatClass = possibleClasses.get(seatClass);
		} else {
			this.seatClass = SeatClass.ECONOMY;
		}
	}
}
