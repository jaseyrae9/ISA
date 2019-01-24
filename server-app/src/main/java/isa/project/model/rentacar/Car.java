package isa.project.model.rentacar;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "car")
public class Car implements Serializable {
	private static final long serialVersionUID = -9158075503665417754L;

	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)	
	@JoinColumn(name="rentacar_company_id", referencedColumnName="id")
	private RentACarCompany rentACarCompany;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "brand", nullable = false)
	private String brand;
	
	@Column(name = "model", nullable = false)
	private String model;	
	
	@Column(name = "yearOfProduction", nullable = false)
	private Integer yearOfProduction;
	
	@Column(name = "seatsNumber", nullable = false)
	private Integer seatsNumber;	
	
	@Column(name = "doorsNumber", nullable = false)
	private Integer doorsNumber;
	
	@Column(name = "price", nullable = false)
	private Double price;
	
	@Column(name = "active")
	private Boolean active;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "car", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CarReservation> carReservations;
	
	public Car() {
		//super();
	}
	
	public Car(RentACarCompany rentACarCompany, String brand, String model, Integer yearOfProduction, Integer seatsNumber, Integer doorsNumber,
			Double price, String type) {
		super();
		this.rentACarCompany = rentACarCompany;
		this.brand = brand;
		this.model = model;
		this.yearOfProduction = yearOfProduction;
		this.seatsNumber = seatsNumber;
		this.doorsNumber = doorsNumber;
		this.price = price;
		this.type = type;
		this.active = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getYearOfProduction() {
		return yearOfProduction;
	}

	public void setYearOfProduction(Integer yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}

	public Integer getSeatsNumber() {
		return seatsNumber;
	}

	public void setSeatsNumber(Integer seatsNumber) {
		this.seatsNumber = seatsNumber;
	}

	public Integer getDoorsNumber() {
		return doorsNumber;
	}

	public void setDoorsNumber(Integer doorsNumber) {
		this.doorsNumber = doorsNumber;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public RentACarCompany getRentACarCompany() {
		return rentACarCompany;
	}

	public void setRentACarCompany(RentACarCompany rentACarCompany) {
		this.rentACarCompany = rentACarCompany;
	}
	
	public Set<CarReservation> getCarReservations() {
		return carReservations;
	}

	public void setCarReservations(Set<CarReservation> carReservations) {
		this.carReservations = carReservations;
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
		Car other = (Car) obj;
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
		return "Car [id=" + id + ", rentACarCompany=" + rentACarCompany + ", type=" + type + ", brand=" + brand
				+ ", model=" + model + ", yearOfProduction=" + yearOfProduction + ", seatsNumber=" + seatsNumber
				+ ", doorsNumber=" + doorsNumber + ", price=" + price + ", active=" + active + "]";
	}

}
