package isa.project.model.aircompany;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.model.users.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class FlightReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flight_id", referencedColumnName = "id", nullable = false)
	private Flight flight;
	
	@JsonManagedReference(value="tickets")
	@OneToMany(mappedBy = "flightReservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TicketReservation> ticketReservations;
	
	@JsonBackReference(value = "reservation-flight-reservation")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "flightReservation", orphanRemoval = true)
	private Reservation reservation;
	
	public FlightReservation(Flight flight) {
		super();
		this.ticketReservations = new HashSet<>();
		this.flight = flight;
	}
	
	public void addTicketReservation(TicketReservation reservation) {
		this.ticketReservations.add(reservation);
		reservation.setFlightReservation(this);
	}		
}
