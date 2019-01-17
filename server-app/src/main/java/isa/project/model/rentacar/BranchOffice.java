package isa.project.model.rentacar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "branch_office")
public class BranchOffice {

	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="rentacar_company_id", referencedColumnName="id")
	private RentACarCompany rentACarCompany;
	
	@Column(name = "branch_office_name", nullable = false)
	private String name;
	
	public BranchOffice() {
		
	}

	public BranchOffice(RentACarCompany rentACarCompany, String name) {
		super();
		this.rentACarCompany = rentACarCompany;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RentACarCompany getRentACarCompany() {
		return rentACarCompany;
	}

	public void setRentACarCompany(RentACarCompany rentACarCompany) {
		this.rentACarCompany = rentACarCompany;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "BranchOffice [id=" + id + ", rentACarCompany=" + rentACarCompany + ", name=" + name + "]";
	}
	
}