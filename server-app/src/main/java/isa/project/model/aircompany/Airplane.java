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
import javax.persistence.Table;

import isa.project.dto.aircompany.AirplaneDTO;
import isa.project.dto.aircompany.SeatDTO;

@Entity
@Table(name = "airplanes")
public class Airplane implements Serializable {
	private static final long serialVersionUID = 6474876018633877707L;

	public enum AirplaneStatus {IN_PROGRESS, ACTIVE, DELETED};
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="air_company_id", referencedColumnName="id")
	private AirCompany airCompany;
	
	@Column(nullable = false)
	private String name;	
	
	@Column(nullable = false)
	private AirplaneStatus status;
	
	@Column(nullable = false)
	private Integer rowNum;
	
	@Column(nullable = false)
	private Integer colNum;
	
	@Column(nullable = false)
	private Integer seatsPerCol;
	
	@OneToMany(mappedBy = "airplane", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Seat> seats;	
	
	public Airplane() {
		
	}
	
	public Airplane(AirplaneDTO airplaneDTO) {
		this.name = airplaneDTO.getName();
		this.colNum = airplaneDTO.getColNum();
		this.rowNum = airplaneDTO.getRowNum();
		this.seatsPerCol = airplaneDTO.getSeatsPerCol();
		this.seats = new ArrayList<>();
		for(SeatDTO seatDTO:airplaneDTO.getSeats()) {
			Seat seat = new Seat(seatDTO);
			seat.setAirplane(this);
			this.seats.add(seat);
		}
	}
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public AirCompany getAirCompany() {
		return airCompany;
	}

	public void setAirCompany(AirCompany airCompany) {
		this.airCompany = airCompany;
	}		

	public AirplaneStatus getStatus() {
		return status;
	}

	public void setStatus(AirplaneStatus status) {
		this.status = status;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}	

	public Integer getSeatsPerCol() {
		return seatsPerCol;
	}

	public void setSeatsPerCol(Integer seatsPerCol) {
		this.seatsPerCol = seatsPerCol;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
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
