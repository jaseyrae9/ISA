package isa.project.model.aircompany;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import isa.project.model.users.Customer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FriendInvite {
	public enum FriendInviteStatus {PENDING, ACCEPTED, REFUSED};	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="ticket_reservation_id", referencedColumnName="id", nullable = false)
	private TicketReservation ticketReservation;
	
	@JsonBackReference(value = "customer-invites")
	@OneToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="friend", referencedColumnName="id", nullable = false)
	private Customer friend;
	
	@CreationTimestamp
	@Column(nullable = false)
	private Date sent;
	
	@Column(nullable = false)
	private FriendInviteStatus status;
	
	public FriendInvite() {
		this.status = FriendInviteStatus.PENDING;
	}
	
	public FriendInvite(TicketReservation ticketReservation, Customer friend) {
		this.status = FriendInviteStatus.PENDING;
		this.ticketReservation = ticketReservation;
		ticketReservation.setInvitedFriend(this);
		this.friend = friend;
		friend.getInvites().add(this);
	}
	
	public void setTicketReservation(TicketReservation ticketReservation) {
		this.ticketReservation = ticketReservation;
		ticketReservation.setInvitedFriend(this);
	}
}
