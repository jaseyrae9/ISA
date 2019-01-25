package isa.project.model.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "locations")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Address can not be blank.")
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private double lon;
	
	@Column(nullable = false)
	private double lat; 
	
	public Location() {
		lat = 0;
		lon = 0;
	}
	
	public Location(String address) {
		lat = 0;
		lon = 0;
		this.address = address;
	}

	public Location(Long id, @NotBlank(message = "Address can not be blank.") String address, double lon, double lat) {
		super();
		this.id = id;
		this.address = address;
		this.lon = lon;
		this.lat = lat;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
}
