package isa.project.model.hotel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "floor", nullable = false)
	private Integer floor;

	@Column(name = "roomNumber", nullable = false)
	private Integer roomNumber;

	@Column(name = "numberOfBeds", nullable = false)
	private Integer numberOfBeds;

	@Column(name = "price", nullable = false)
	private Double price;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "active")
	private Boolean active;
	

	public Room() {
		super();
	}
	
	public Room(Integer floor, Integer roomNumber, Integer numberOfBeds, Double price, String type) {
		super();
		this.floor = floor;
		this.roomNumber = roomNumber;
		this.numberOfBeds = numberOfBeds;
		this.price = price;
		this.type = type;
		this.active = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((floor == null) ? 0 : floor.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numberOfBeds == null) ? 0 : numberOfBeds.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((roomNumber == null) ? 0 : roomNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (floor == null) {
			if (other.floor != null)
				return false;
		} else if (!floor.equals(other.floor))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numberOfBeds == null) {
			if (other.numberOfBeds != null)
				return false;
		} else if (!numberOfBeds.equals(other.numberOfBeds))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (roomNumber == null) {
			if (other.roomNumber != null)
				return false;
		} else if (!roomNumber.equals(other.roomNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", floor=" + floor + ", roomNumber=" + roomNumber + ", numberOfBeds=" + numberOfBeds
				+ ", price=" + price + "]";
	}
	
}
