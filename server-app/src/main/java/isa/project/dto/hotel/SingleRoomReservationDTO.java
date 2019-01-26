package isa.project.dto.hotel;


import isa.project.model.hotel.SingleRoomReservation;

public class SingleRoomReservationDTO {
	
	private Integer id;
	
	private RoomDTO room;
	
	private RoomReservationDTO roomReservation;

	
	public SingleRoomReservationDTO() {
		
	}
	
	public SingleRoomReservationDTO(SingleRoomReservation reservation) {
		System.out.println("pozvan");
		this.id = reservation.getId();
		//this.room = new RoomDTO(reservation.getRoom()); 
		//System.out.println("room id " + this.room.getId());
		this.roomReservation = new RoomReservationDTO(reservation.getRoomReservation());
		//System.out.println(this.roomReservation);
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
	
	


}
