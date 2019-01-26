package isa.project.dto.aircompany;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FlightDTO {
	@NotNull(message = "Seat class can not be empty.")
	private Integer airplaneId;
	
	@NotBlank(message = "Destinations can not be blank")
	@Size(min = 2, message = "There must be at least two validations")
	private List<Long> destinations;
	
	@NotNull(message = "Departure time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date startDateAndTime;
	
	@NotNull(message = "Arrival time can not be empty.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date endDateAndTime;
	
	@NotNull(message = "Length can not be empty.")
	@Min(value = 0, message = "Minimal length is 0.")
	private Double length;
	
	public FlightDTO() {
		
	}

	public Integer getAirplaneId() {
		return airplaneId;
	}

	public void setAirplaneId(Integer airplaneId) {
		this.airplaneId = airplaneId;
	}

	public List<Long> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Long> destinations) {
		this.destinations = destinations;
	}

	public Date getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(Date startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}

	public Date getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(Date endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

}
