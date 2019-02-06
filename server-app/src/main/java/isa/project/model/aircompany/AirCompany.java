package isa.project.model.aircompany;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "air_company")
public class AirCompany {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = true)
	private String description;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="location_id", nullable = false)
	private Location location;
	
	@OneToMany(mappedBy = "airCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Destination> destinations;
	
	@OneToMany(mappedBy = "airCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Airplane> airplanes;
	
	@OneToMany(mappedBy = "airCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Flight> flights;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AdditionalService> baggageInformation;
	
	@OrderBy("creationDate DESC")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TicketForFastReservation> ticketForFastReservations;
	
	@Column(name = "totalRating")
	private Integer totalRating;
	
	@Column(name = "ratingCount")
	private Integer ratingCount;

	public AirCompany(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.totalRating = 5;
		this.ratingCount = 1;
	}
	
	public Set<Destination> getDestinations() {
		if(destinations != null)
			return destinations.stream().filter(d -> d.getActive()).collect(Collectors.toSet());
		else
			return null;
	}
	
	public Set<AdditionalService> getBaggageInformation() {
		if(baggageInformation != null)
			return baggageInformation.stream().filter(p -> p.getActive()).collect(Collectors.toSet()); // Da vraca samo aktivne
		else
			return null;
	}
	
	public void addBaggageInformation(AdditionalService baggageInformation) {
		this.baggageInformation.add(baggageInformation);
	}

	public void setTicketForFastReservations(List<TicketForFastReservation> ticketForFastReservations) {
		this.ticketForFastReservations = ticketForFastReservations;
	}
	
	public void removeTicket(TicketForFastReservation ticket) {
		ticket.setAirCompany(null);
		this.ticketForFastReservations.remove(ticket);
	}
	
	public void incrementRatingCount() {
		this.ratingCount++;
	}
	
	public void addToTotalRating(Integer x) {
		this.totalRating += x;
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
		AirCompany other = (AirCompany) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AvioCompany [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
