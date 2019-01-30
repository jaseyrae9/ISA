package isa.project.model.rentacar;

import java.sql.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
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

import isa.project.model.users.Customer;


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
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private BranchOffice pickUpBranchOffice;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	private BranchOffice dropOffBranchOffice;
	
	@Column(name = "active")
	private Boolean active;
	
	@JsonBackReference(value="car-reservations")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="car_id", referencedColumnName="id")
	private Car car;
	
	@JsonBackReference(value = "customer-car-reservations")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
	@Column(name = "isRated")
	private Boolean isRated;
	
	public CarReservation() {
		
	}

	public CarReservation(Customer customer, Car car, Date pickUpDate, Date dropOffDate, BranchOffice pickUpBranchOffice,
			BranchOffice dropOffBranchOffice) {
		super();
		this.customer = customer;
		this.car = car;
		this.pickUpDate = pickUpDate;
		this.dropOffDate = dropOffDate;
		this.pickUpBranchOffice = pickUpBranchOffice;
		this.dropOffBranchOffice = dropOffBranchOffice;
		this.active = true;
		this.isRated = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	public Date getDropOffDate() {
		return dropOffDate;
	}

	public void setDropOffDate(Date dropOffDate) {
		this.dropOffDate = dropOffDate;
	}

	public BranchOffice getPickUpBranchOffice() {
		return pickUpBranchOffice;
	}

	public void setPickUpBranchOffice(BranchOffice pickUpBranchOffice) {
		this.pickUpBranchOffice = pickUpBranchOffice;
	}

	public BranchOffice getDropOffBranchOffice() {
		return dropOffBranchOffice;
	}

	public void setDropOffBranchOffice(BranchOffice dropOffBranchOffice) {
		this.dropOffBranchOffice = dropOffBranchOffice;
	}
			
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getIsRated() {
		return isRated;
	}

	public void setIsRated(Boolean isRated) {
		this.isRated = isRated;
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
