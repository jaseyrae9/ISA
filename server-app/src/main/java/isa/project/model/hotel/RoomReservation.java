package isa.project.model.hotel;

import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.model.shared.AdditionalService;
import isa.project.model.users.Customer;

@Entity
@Table(name = "room_reservation")
public class RoomReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "check_in_date", nullable = false)
	private Date checkInDate;
	
	@Column(name = "check_out_date", nullable = false)
	private Date checkOutDate;
	
	@ManyToMany
	private Set<AdditionalService> additionalServices;
	
	@JsonManagedReference(value = "single-room-reservations")
	@OneToMany(mappedBy = "roomReservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SingleRoomReservation> singleRoomReservations;
	
	private Boolean active;
	
	@JsonBackReference(value = "room-reservations")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="customer_id", referencedColumnName="id")
	private Customer customer;
	
	
	public RoomReservation() {	
	}

	public RoomReservation(Customer customer, Date checkInDate, Date checkOutDate) {
		super();
		this.customer = customer;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.additionalServices = new HashSet<>();
		this.singleRoomReservations = new HashSet<>();
		this.active = true;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Set<AdditionalService> getAdditionalServices() {
		return additionalServices;
	}

	public void setAdditionalServices(Set<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}
	
	public void addAdditionalService(AdditionalService additionalService) {
		this.additionalServices.add(additionalService);
	}
		
	public Set<SingleRoomReservation> getSingleRoomReservations() {
		return singleRoomReservations;
	}

	public void setSingleRoomReservations(Set<SingleRoomReservation> singleRoomReservations) {
		this.singleRoomReservations = singleRoomReservations;
	}
	
	public void addSingleRoomReservation(SingleRoomReservation singleRoomReservation) {
		this.singleRoomReservations.add(singleRoomReservation);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
		RoomReservation other = (RoomReservation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoomReservation [id=" + id + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate
				+ ", additionalServices=" + additionalServices + "]";
	}
}
