package isa.project.model.rentacar;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import isa.project.model.users.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "car_reservation")
public class CarReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "pick_up_date", nullable = false)
	private Date pickUpDate;
	
	@Column(name = "drop_off_date", nullable = false)
	private Date dropOffDate;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	private BranchOffice pickUpBranchOffice;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	private BranchOffice dropOffBranchOffice;
	
	@Column(name = "active")
	private Boolean active;
	
	@JsonBackReference(value="car-reservations")
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="car_id", referencedColumnName="id")
	private Car car;	
	
	@JsonBackReference(value = "reservation-car-reservation")
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "carReservation")
	private Reservation reservation;
	
	@Column(name = "isCarRated")
	private Boolean isCarRated;
	
	@Column(name = "isCompanyRated")
	private Boolean isCompanyRated;	


	public CarReservation(Car car, Date pickUpDate, Date dropOffDate, BranchOffice pickUpBranchOffice,
			BranchOffice dropOffBranchOffice) {
		super();
		this.car = car;
		this.pickUpDate = pickUpDate;
		this.dropOffDate = dropOffDate;
		this.pickUpBranchOffice = pickUpBranchOffice;
		this.dropOffBranchOffice = dropOffBranchOffice;
		this.active = true;
		this.isCarRated = false;
		this.isCompanyRated = false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarReservation other = (CarReservation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CarReservation [id=" + id + ", pickUpDate=" + pickUpDate + ", dropOffDate=" + dropOffDate
				+ ", pickUpBranchOffice=" + pickUpBranchOffice + ", dropOffBranchOffice=" + dropOffBranchOffice + "]";
	}	
	
	
	
}
