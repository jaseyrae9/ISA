package isa.project.model.users;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.model.hotel.RoomReservation;
import isa.project.model.rentacar.CarReservation;

@Entity
@Table(name = "reservation")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonBackReference(value = "customer-reservations")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)	
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
    @JsonManagedReference(value = "reservation-car-reservation")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservation", orphanRemoval = true)
	private CarReservation carReservation;
	
	@JsonManagedReference(value = "reservation-room-reservation")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reservation", orphanRemoval = true)
	private RoomReservation roomReservation;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Date creationDate;
	
	public Reservation() {
		
	}

	public Reservation(Customer customer, CarReservation carReservation, RoomReservation roomReservation) {
		super();
		this.customer = customer;
		this.carReservation = carReservation;
		this.roomReservation = roomReservation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CarReservation getCarReservation() {
		return carReservation;
	}

	public void setCarReservation(CarReservation carReservation) {
		this.carReservation = carReservation;
	}

	public RoomReservation getRoomReservation() {
		return roomReservation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}	
		
}
