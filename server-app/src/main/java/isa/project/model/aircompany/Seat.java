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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "seats")
public class Seat implements Serializable{
	public enum SeatClass {ECONOMY, PREMIUM_ECONOMY, BUSSINESS, FIRST};
	
	private static final long serialVersionUID = 2817053057255132050L;
	
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Setter
	@ManyToOne
	@JoinColumn(name="airplane_id", referencedColumnName="id")
	private Airplane airplane;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private SeatClass seatClass;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer index;
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer rowNum;	
	
	@Getter
	@Setter
	@Column(nullable = false)
	private Integer colNum;
	
	public Seat(SeatDTO seatDTO) {
		this.seatClass = seatDTO.getSeatClass();
		this.rowNum = seatDTO.getRowNum();
		this.colNum = seatDTO.getColNum();
	}

	@JsonIgnore
	public Airplane getAirplane() {
		return airplane;
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
