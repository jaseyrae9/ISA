package isa.project.model.aircompany;

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
import javax.persistence.Table;

import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;


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
	private Set<Flight> flight;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AdditionalService> baggageInformation;
	
	public AirCompany() {
		super();
	}

	public AirCompany(String name, String description) {
		super();
		this.name = name;
		this.description = description;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Destination> getDestinations() {
		if(destinations != null)
			return destinations.stream().filter(d -> d.getActive()).collect(Collectors.toSet());
		else
			return null;
	}

	public void setDestinations(Set<Destination> destinations) {
		this.destinations = destinations;
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

	public void setBaggageInformation(Set<AdditionalService> baggageInformation) {
		this.baggageInformation = baggageInformation;
	}	

	public Set<Airplane> getAirplanes() {
		return airplanes;
	}

	public void setAirplanes(Set<Airplane> airplanes) {
		this.airplanes = airplanes;
	}	
	
	public Set<Flight> getFlight() {
		return flight;
	}

	public void setFlight(Set<Flight> flight) {
		this.flight = flight;
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
