package isa.project.dto.hotel;


import isa.project.model.hotel.SingleRoomReservation;

public class SingleRoomReservationDTO {
	
	private Integer id;
	
	private RoomDTO room;
	
	private RoomReservationDTO roomReservation;

	private Boolean isRoomRated;
	
	private Boolean isHotelRate;

	public SingleRoomReservationDTO() {
		
	}
	
	public SingleRoomReservationDTO(SingleRoomReservation reservation) {
		this.id = reservation.getId();
		this.roomReservation = new RoomReservationDTO(reservation.getRoomReservation());
		this.isRoomRated = reservation.getIsRoomRated();
		this.isHotelRate = reservation.getIsHotelRated();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoomDTO getRoom() {
		return room;
	}

	public void setRoom(RoomDTO room) {
		this.room = room;
	}

	public RoomReservationDTO getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservationDTO roomReservation) {
		this.roomReservation = roomReservation;
	}

	public Boolean getIsRoomRated() {
		return isRoomRated;
	}

	public void setIsRoomRated(Boolean isRoomRated) {
		this.isRoomRated = isRoomRated;
	}

	public Boolean getIsHotelRate() {
		return isHotelRate;
	}

	public void setIsHotelRate(Boolean isHotelRate) {
		this.isHotelRate = isHotelRate;
	}

}
