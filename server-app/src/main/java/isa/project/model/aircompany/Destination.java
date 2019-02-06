package isa.project.model.aircompany;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "destinations")
public class Destination implements Serializable {
	private static final long serialVersionUID = 1850905124075543593L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)	
	@JoinColumn(name="air_company_id", referencedColumnName="id", nullable = false)
	private AirCompany airCompany;

	@Column(length = 3, nullable = false)
	private String label;

	@Column(nullable = false)
	private String country;
	
	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String airportName;
	
	@Column(nullable = false)
	private Boolean active;
	
	public Destination(AirCompany airCompany, String label, String country, String city, String airportName) {
		super();	
		this.airCompany = airCompany;
		this.label = label;
		this.country = country;
		this.city = city;
		this.airportName = airportName;
		this.active = true;
	}	

	@JsonIgnore
	public AirCompany getAirCompany() {
		return airCompany;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airCompany == null) ? 0 : airCompany.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Destination other = (Destination) obj;
		if (airCompany == null) {
			if (other.airCompany != null)
				return false;
		} else if (!airCompany.equals(other.airCompany))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return airportName + ", " + city + ", " + country;
	}	
	
}
