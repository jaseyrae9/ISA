package isa.project.dto.hotel;


import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;

public class SingleRoomReservationDTO {
	
	private Integer id;
	
	//private Room room;
	
	private RoomReservationDTO roomReservation;

	
	public SingleRoomReservationDTO() {
		
	}
	
	public SingleRoomReservationDTO(SingleRoomReservation reservation) {
		this.id = reservation.getId();
		//this.room = reservation.getRoom(); 
		//System.out.println("room id " + this.room.getId());
		this.roomReservation = new RoomReservationDTO(reservation.getRoomReservation());
		System.out.println(this.roomReservation);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public Room getRoom() {
//		return room;
//	}
//
//	public void setRoom(Room room) {
//		this.room = room;
//	}

	public RoomReservationDTO getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservationDTO roomReservation) {
		this.roomReservation = roomReservation;
	}
	
	


}
