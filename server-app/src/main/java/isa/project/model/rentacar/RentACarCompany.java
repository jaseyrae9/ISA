package isa.project.model.rentacar;

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

import isa.project.model.shared.Location;


@Entity
@Table(name = "rent_a_car_company")
public class RentACarCompany {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "RentACarCompanyName", nullable = false)
	private String name;
	
	@Column(name = "RentACarDescription", nullable = true)
	private String description;
	
	@JsonManagedReference(value="rent-a-car")
	@OneToMany(mappedBy = "rentACarCompany", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Car> cars;
	
	@JsonManagedReference(value="branch-offices")
	@OneToMany(mappedBy = "rentACarCompany", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BranchOffice> branchOffices;	
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="location_id", nullable = false)
	private Location location;
	
	@Column(name = "totalRating")
	private Integer totalRating;
	
	@Column(name = "ratingCount")
	private Integer ratingCount;
	
	public RentACarCompany() {
		super();
	}

	public RentACarCompany(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.totalRating = 5; // na pocetku je ocena 5
		this.ratingCount = 1; // jedan glas
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Car> getCars() {
		if(cars != null)
			return cars.stream().filter(p -> p.getActive()).collect(Collectors.toSet()); // Da vraca samo aktivne automobile
		else
			return null;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}

	public Set<BranchOffice> getBranchOffices() {
		if(branchOffices != null)
			return branchOffices.stream().filter(p -> p.getActive()).collect(Collectors.toSet()); // Da vraca samo aktivne
		return null;
	}

	public void setBranchOffices(Set<BranchOffice> branchOffices) {
		this.branchOffices = branchOffices;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getTotalRating() {
		return totalRating;
	}

	public void setTotalRating(Integer totalRating) {
		this.totalRating = totalRating;
	}
	
	public void addToTotalRating(Integer x) {
		this.totalRating += x;
	}

	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
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
		RentACarCompany other = (RentACarCompany) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RentACarCompany [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
			
}
