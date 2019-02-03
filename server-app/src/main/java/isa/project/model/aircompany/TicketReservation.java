package isa.project.model.aircompany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TicketReservation {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	private String passport;
	
	@OneToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="ticket_id", referencedColumnName="id")
	private Ticket ticket;
	
	@JsonBackReference(value="tickets")
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name="flightReservation", referencedColumnName="id")
	private FlightReservation flightReservation;

	public TicketReservation(String firstName, String lastName, String passport, Ticket ticket) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.passport = passport;
		this.ticket = ticket;
	}	
}
