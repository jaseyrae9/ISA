package isa.project.dto.hotel;

import java.util.ArrayList;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.hotel.Room;
import isa.project.model.hotel.SingleRoomReservation;

public class RoomDTO {
	
	private Integer id;
	
	@NotNull(message = "Floor number can not be blank.")
	private Integer floor;
	
	@NotNull(message = "Room number can not be blank.")
	@Min(value = 0, message = "Room number can not be less than zero.")
	private Integer roomNumber;
	
	@NotNull(message = "Number of beds can not be blank.")
	@Min(value = 1, message = "There must be at least one bed.")
	private Integer numberOfBeds;
	
	@NotNull(message = "Price must be entered.")
	@Min(value = 0, message = "Price can not be less than zero.")
	private Double price;
	
	private String type;
	private Boolean active;	
	
	private Double averageRating;	
	
	private ArrayList<SingleRoomReservationDTO> reservations = new ArrayList<>();
	
	public RoomDTO()
	{
		
	}

	public RoomDTO(Room room)
	{
		this.id = room.getId();
		this.floor = room.getFloor();
		this.roomNumber = room.getRoomNumber();
		this.numberOfBeds = room.getNumberOfBeds();
		this.price = room.getPrice();
		this.type = room.getType();
		this.active = room.getActive();
		this.averageRating = (room.getTotalRating() / (double) room.getRatingCount());
		if(room.getSingleRoomReservations() != null) {
			for(SingleRoomReservation srr: room.getSingleRoomReservations()) {
				this.reservations.add(new SingleRoomReservationDTO(srr));
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Integer getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setNumberOfBeds(Integer numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ArrayList<SingleRoomReservationDTO> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<SingleRoomReservationDTO> reservations) {
		this.reservations = reservations;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}
	
}
