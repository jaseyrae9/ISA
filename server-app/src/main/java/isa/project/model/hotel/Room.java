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

	public Room() {
	}
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
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
	
	public Set<SingleRoomReservation> getSingleRoomReservations() {
		return singleRoomReservations;
	}

	public void setSingleRoomReservations(Set<SingleRoomReservation> singleRoomReservations) {
		this.singleRoomReservations = singleRoomReservations;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public void increaseVersion() {
		this.version++;
	}
	
	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	public Integer getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(Integer totalRating) {
		this.totalRating = totalRating;
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
