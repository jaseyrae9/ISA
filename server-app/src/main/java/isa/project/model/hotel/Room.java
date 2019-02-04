package isa.project.model.hotel;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room")
public class Room implements Serializable{
	private static final long serialVersionUID = 8547192770831169696L;

	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonBackReference(value="hotel")
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="hotel_id", referencedColumnName="id")
	private Hotel hotel;

	@JsonManagedReference(value="single-room-reservation")
	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<SingleRoomReservation> singleRoomReservations;
	
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
	
	@Column(name = "totalRating")
	private Integer totalRating;
	
	@Column(name = "ratingCount")
	private Integer ratingCount;
	
	@Version
	private Long version;
	
	public Room(Hotel hotel, Integer floor, Integer roomNumber, Integer numberOfBeds, Double price, String type) {
		super();
		this.hotel = hotel;
		this.floor = floor;
		this.roomNumber = roomNumber;
		this.numberOfBeds = numberOfBeds;
		this.price = price;
		this.type = type;
		this.active = true;
		this.totalRating = 5; // na pocetku je ocena 5
		this.ratingCount = 1; // jedan glas
	}

	public Room(Room room) {
		super();
		this.id = room.id;
		this.hotel = room.hotel;
		this.floor = room.floor;
		this.roomNumber = room.roomNumber;
		this.numberOfBeds = room.numberOfBeds;
		this.price = room.price;
		this.type = room.type;
		this.active = room.active;
		this.totalRating = room.totalRating; 
		this.ratingCount = room.ratingCount; 
	}
	
	public void increaseVersion() {
		this.version++;
	}
			
	public void incrementRatingCount() {
		this.ratingCount++;
	}
	
	public void addToTotalRating(Integer x) {
		this.totalRating += x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", hotel=" + hotel + ", floor=" + floor + ", roomNumber=" + roomNumber
				+ ", numberOfBeds=" + numberOfBeds + ", price=" + price + ", type=" + type + ", active=" + active + "]";
	}
	
}
