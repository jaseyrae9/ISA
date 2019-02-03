package isa.project.model.aircompany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import isa.project.dto.aircompany.AirplaneDTO;
import isa.project.dto.aircompany.SeatDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "airplanes")
public class Airplane implements Serializable {
	private static final long serialVersionUID = 6474876018633877707L;

	public enum AirplaneStatus {IN_PROGRESS, ACTIVE, DELETED};
	

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="air_company_id", referencedColumnName="id", nullable = false)
	private AirCompany airCompany;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private String name;	
	
	@Getter
	@Setter
	@Column(nullable = false)
	private AirplaneStatus status;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer rowNum;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer colNum;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer seatsPerCol;
	
	@OrderColumn(name = "index")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "airplane", orphanRemoval = true)
	private List<Seat> seats;	
		
	public Airplane(AirplaneDTO airplaneDTO) {
		this.name = airplaneDTO.getName();
		this.colNum = airplaneDTO.getColNum();
		this.rowNum = airplaneDTO.getRowNum();
		this.seatsPerCol = airplaneDTO.getSeatsPerCol();
		this.seats = new ArrayList<>();
		int index = 0;
		for(SeatDTO seatDTO:airplaneDTO.getSeatsAsArray()) {
			Seat seat = new Seat(seatDTO);
			seat.setAirplane(this);
			seat.setIndex(index);
			this.seats.add(seat);
			index += 1;
		}
	}		
	
	@JsonIgnore
	public AirCompany getAirCompany() {
		return airCompany;
	}

	@JsonIgnore
	public List<Seat> getSeats() {
		return seats;
	}
	
	public void removeSeat(Seat seat) {
		seat.setAirplane(null);
		this.seats.remove(seat);
	}

	public void removeSeatsStartingFromIndex(int index) {
		for(int i = this.seats.size() - 1; i >= index; --i) {
			   removeSeat(this.seats.get(i));
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
		Airplane other = (Airplane) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
