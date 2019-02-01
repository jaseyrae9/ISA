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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TicketForFastReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="air_company_id", referencedColumnName="id", nullable = false)
	private AirCompany airCompany;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="ticket_id", referencedColumnName="id", nullable = false)
	private Ticket ticket;

	@CreationTimestamp
	@Column(nullable = false)
	private Date creationDate;
	
	public TicketForFastReservation() {
		
	}

	public TicketForFastReservation(AirCompany airCompany, Ticket ticket) {
		super();
		this.airCompany = airCompany;
		this.ticket = ticket;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public AirCompany getAirCompany() {
		return airCompany;
	}

	public void setAirCompany(AirCompany airCompany) {
		this.airCompany = airCompany;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	
}
