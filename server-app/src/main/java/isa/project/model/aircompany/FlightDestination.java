package isa.project.model.aircompany;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "flight_destinations")
public class FlightDestination implements Serializable{
	private static final long serialVersionUID = 867798187874772312L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Column
	private Integer index;	
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="flight_id", referencedColumnName="id")
	private Flight flight;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="destination_id", referencedColumnName="id")
	private Destination destination;	
	
	@JsonIgnore()
	public Flight getFlight() {
		return flight;
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
		FlightDestination other = (FlightDestination) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
