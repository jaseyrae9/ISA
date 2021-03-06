package isa.project.model.aircompany;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import isa.project.model.users.FriendInvite;
import isa.project.model.users.FriendInvite.FriendInviteStatus;
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
	
	@Column
	private String passport;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="ticket_id", referencedColumnName="id")
	private Ticket ticket;
		
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="flightReservation", referencedColumnName="id")
	private FlightReservation flightReservation;	
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "ticketReservation")
	private FriendInvite invitedFriend;
	
	@Setter
	@CreationTimestamp
	@Column(nullable = false)
	private Date saleDate;

	public TicketReservation(String firstName, String lastName, String passport, Ticket ticket) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.passport = passport;
		this.ticket = ticket;
	}	
	
	@JsonIgnore	
	public FriendInvite getInvitedFriend() {
		return this.invitedFriend;
	}
	
	@JsonIgnore	
	public FlightReservation getFlightReservation() {
		return this.flightReservation;
	}
	
	public FriendInviteStatus getInviteStatus() {
		if(this.invitedFriend != null) {
			return this.invitedFriend.getStatus();
		}
		return null;
	}
}
