package isa.project.model.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Country can not be blank.")
	@Column(nullable = false)
	private String country;
	
	@NotBlank(message = "City can not be blank.")
	@Column(nullable = false)
	private String city;
	
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

	@Override
	public String toString() {
		return address + ", " + city + ", " + country;
	}

}
