package isa.project.model.rentacar;

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

import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "branch_office")
public class BranchOffice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonBackReference(value="branch-offices")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="rentacar_company_id", referencedColumnName="id")
	private RentACarCompany rentACarCompany;
	
	@Column(name = "branch_office_name", nullable = false)
	private String name;
	
	@Column(name = "active")
	private Boolean active;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="location_id", nullable = false)
	private Location location;	
	
	public BranchOffice(RentACarCompany rentACarCompany, String name) {
		super();
		this.rentACarCompany = rentACarCompany;
		this.name = name;
		this.active = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rentACarCompany == null) ? 0 : rentACarCompany.hashCode());
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
		BranchOffice other = (BranchOffice) obj;
		if (rentACarCompany == null) {
			if (other.rentACarCompany != null)
				return false;
		} else if (!rentACarCompany.equals(other.rentACarCompany))
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
		return name + ", " + location.toString();
	}
	
}
