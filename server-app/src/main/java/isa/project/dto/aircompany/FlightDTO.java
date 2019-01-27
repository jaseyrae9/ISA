package isa.project.dto.aircompany;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author Milica
 *
 */
public class FlightDTO {
	@NotNull(message = "Seat class can not be empty.")
	private Integer airplaneId;
		
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
	
	@NotNull(message = "Maximal number of carry on bags can not be empty.")
	@Min(value = 0, message = "Maximal number of carry on bags can not be less than 0.")
	private Integer maxCarryOnBags;
	
	@NotNull(message = "Maximal number od checked bags can not be empty.")
	@Min(value = 0, message = "Maximal number od checked bags can not be less than 0.")
	private Integer maxCheckedBags;
	
	@NotNull(message = "Additional services available can not be blank.")
	private Boolean additionalServicesAvailable;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double economyPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double premiumEconomyPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double bussinessPrice;
	
	@NotNull(message = "Price can not be empty.")
	@Min(value = 0, message = "Minimal price is 0.")
	private Double firstPrice;
	
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

	public Integer getMaxCarryOnBags() {
		return maxCarryOnBags;
	}

	public void setMaxCarryOnBags(Integer maxCarryOnBags) {
		this.maxCarryOnBags = maxCarryOnBags;
	}

	public Integer getMaxCheckedBags() {
		return maxCheckedBags;
	}

	public void setMaxCheckedBags(Integer maxCheckedBags) {
		this.maxCheckedBags = maxCheckedBags;
	}

	public Boolean getAdditionalServicesAvailable() {
		return additionalServicesAvailable;
	}

	public void setAdditionalServicesAvailable(Boolean additionalServicesAvailable) {
		this.additionalServicesAvailable = additionalServicesAvailable;
	}

	public Double getEconomyPrice() {
		return economyPrice;
	}

	public void setEconomyPrice(Double ecomomyPrice) {
		this.economyPrice = ecomomyPrice;
	}

	public Double getPremiumEconomyPrice() {
		return premiumEconomyPrice;
	}

	public void setPremiumEconomyPrice(Double premiumEconomyPrice) {
		this.premiumEconomyPrice = premiumEconomyPrice;
	}

	public Double getBussinessPrice() {
		return bussinessPrice;
	}

	public void setBussinessPrice(Double bussinessPrice) {
		this.bussinessPrice = bussinessPrice;
	}

	public Double getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
}
