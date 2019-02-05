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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.model.shared.AdditionalService;
import isa.project.model.users.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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

	@JsonBackReference(value = "reservation-room-reservation")
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "roomReservation")
	private Reservation reservation;

	@Column(name = "isHotelRated")
	private Boolean isHotelRated;

	public RoomReservation(Date checkInDate, Date checkOutDate) {
		super();
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.additionalServices = new HashSet<>();
		this.singleRoomReservations = new HashSet<>();
		this.isHotelRated = false;
		this.active = true;
	}

	public void addAdditionalService(AdditionalService additionalService) {
		this.additionalServices.add(additionalService);
	}

	public void addSingleRoomReservation(SingleRoomReservation singleRoomReservation) {
		this.singleRoomReservations.add(singleRoomReservation);
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
