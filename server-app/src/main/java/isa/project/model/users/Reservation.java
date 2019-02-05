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

import isa.project.model.aircompany.FlightReservation;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.rentacar.CarReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "reservation")
public class Reservation {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
		
	@JsonBackReference(value = "customer-reservations")
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
	@JsonManagedReference(value = "reservation-flight-reservation")	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)	
	@JoinColumn(name="flight_reservation_id", referencedColumnName="id")
	private FlightReservation flightReservation;
	
    @JsonManagedReference(value = "reservation-car-reservation")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)	
	@JoinColumn(name="car_reservation_id", referencedColumnName="id")
	private CarReservation carReservation;
	
	@JsonManagedReference(value = "reservation-room-reservation")	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)	
	@JoinColumn(name="room_reservation_id", referencedColumnName="id")
	private RoomReservation roomReservation;
	
	@Setter
	@CreationTimestamp
	@Column(nullable = false)
	private Date creationDate;

	public Reservation(Customer customer, CarReservation carReservation, RoomReservation roomReservation) {
		super();
		this.customer = customer;
		this.carReservation = carReservation;
		this.roomReservation = roomReservation;
	}

	public void setCarReservation(CarReservation carReservation) {
		this.carReservation = carReservation;
		carReservation.setReservation(this);
	}

	public void setRoomReservation(RoomReservation roomReservation) {
		this.roomReservation = roomReservation;
		roomReservation.setReservation(this);
	}

	public void setFlightReservation(FlightReservation flightReservation) {
		this.flightReservation = flightReservation;
		flightReservation.setReservation(this);
	}
		
	public void setCustomer(Customer customer) {
		this.customer = customer;
		customer.getReservations().add(this);
	}
}
