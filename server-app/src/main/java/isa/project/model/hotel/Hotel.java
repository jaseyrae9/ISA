package isa.project.model.hotel;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hotel")
public class Hotel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "HotelName", nullable = false)
	private String name;
	
	@Column(name = "HotelDescription", nullable = true)
	private String description;
	
	@JsonManagedReference(value="hotel")
	@OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Room> rooms;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<AdditionalService> additionalServices;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="location_id", nullable = false)
	private Location location;
	
	@Column(name = "totalRating")
	private Integer totalRating;
	
	@Column(name = "ratingCount")
	private Integer ratingCount;
	
	public Hotel(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.totalRating = 5; // na pocetku je ocena 5
		this.ratingCount = 1; // jedan glas
	}	
	
	public Set<Room> getRooms() {
		if(rooms != null)
			return rooms.stream().filter(p -> p.getActive()).collect(Collectors.toSet()); // Da vraca samo aktivne
		else
			return null;
	}

	
	public Set<AdditionalService> getAdditionalServices() {
		if(additionalServices != null)
			return additionalServices.stream().filter(p -> p.getActive()).collect(Collectors.toSet()); // Da vraca samo aktivne
		else
			return null;
	}
	
	public void addAdditionalService(AdditionalService as) {
		additionalServices.add(as);
	}

	public void addToTotalRating(Integer x) {
		this.totalRating += x;
	}	
	
	public void incrementRatingCount() {
		this.ratingCount++;
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
		Hotel other = (Hotel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Hotel [id=" + id + ", name=" + name + ", description=" + description + "]";
	}	
	
	
}
