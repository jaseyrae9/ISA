package isa.project.model.hotel;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "single_room_reservation")
public class SingleRoomReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonBackReference(value="single-room-reservation")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "room_id", referencedColumnName = "id")
	private Room room;

	@JsonBackReference(value = "single-room-reservations")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "room_reservation_id", referencedColumnName = "id")
	private RoomReservation roomReservation;

	@Column(name = "isRoomRated")
	private Boolean isRoomRated;
	
	public SingleRoomReservation() {

	}

	public SingleRoomReservation(Room room, RoomReservation roomReservation) {
		this.room = room;
		this.roomReservation = roomReservation;
		this.isRoomRated = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public RoomReservation getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}

	public Boolean getIsRoomRated() {
		return isRoomRated;
	}

	public void setIsRoomRated(Boolean isRoomRated) {
		this.isRoomRated = isRoomRated;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleRoomReservation other = (SingleRoomReservation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
