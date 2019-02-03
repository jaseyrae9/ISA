package isa.project.model.aircompany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
	public enum TicketStatus {UNAVIABLE, AVAILABLE, RESERVED, FAST_RESERVATION}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference(value="flight")
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="flight", referencedColumnName="id")
	private Flight flight;	

	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="seat", referencedColumnName="id", nullable = false)
	private Seat seat;
	
	@Column(nullable = false)
	private Double price;
	
	@Column
	private Double discount;
	
	@Column(nullable = false)
	private TicketStatus status;
	
	@Column(nullable = false)
	private Integer index;
		
	public Ticket() {
		this.status = TicketStatus.AVAILABLE;
	}

	public Ticket(Flight flight, Seat seat, Double price) {
		super();
		this.status = TicketStatus.AVAILABLE;
		this.flight = flight;
		this.seat = seat;
		this.price = price;
	}	

	public void setDiscount(Double discount) {
		if(discount > price) {
			this.discount = this.price;
		} else {
			this.discount = discount;
		}
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
		Ticket other = (Ticket) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
