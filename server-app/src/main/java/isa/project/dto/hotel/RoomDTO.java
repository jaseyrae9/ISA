package isa.project.dto.hotel;

import isa.project.model.hotel.Room;

public class RoomDTO {
	
	private Integer id;
	private Integer floor;
	private Integer roomNumber;
	private Integer numberOfBeds;
	private Double price;
	
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
}
