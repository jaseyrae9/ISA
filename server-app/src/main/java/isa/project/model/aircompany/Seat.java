package isa.project.model.aircompany;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import isa.project.dto.aircompany.SeatDTO;

@Entity
@Table(name = "seats")
public class Seat implements Serializable{
	public enum SeatClass {ECONOMY, PREMIUM_ECONOMY, BUSSINESS, FIRST};
	
	private static final long serialVersionUID = 2817053057255132050L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="airplane_id", referencedColumnName="id", nullable = false)
	private Airplane airplane;
	
	@Column(nullable = false)
	private SeatClass seatClass;
	
	@Column(nullable = false)
	private Integer index;
	
	@Column(nullable = false)
	private Integer rowNum;	
	
	@Column(nullable = false)
	private Integer colNum;
	
	public Seat() {
		
	}
	
	public Seat(SeatDTO seatDTO) {
		this.seatClass = seatDTO.getSeatClass();
		this.rowNum = seatDTO.getRowNum();
		this.colNum = seatDTO.getColNum();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	public Airplane getAirplane() {
		return airplane;
	}

	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}
	
	public SeatClass getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
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
		Seat other = (Seat) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
