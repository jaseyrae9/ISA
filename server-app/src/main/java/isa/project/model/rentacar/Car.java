package isa.project.model.rentacar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "car")
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
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
	private Integer price;
	
	@Column(name = "active")
	private Boolean active;
	
	public Car() {
		super();
	}
	
	public Car(String brand, String model, Integer yearOfProduction, Integer seatsNumber, Integer doorsNumber,
			Integer price, String type) {
		super();
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", brand=" + brand + ", model=" + model + ", yearOfProduction=" + yearOfProduction
				+ ", seatsNumber=" + seatsNumber + ", doorsNumber=" + doorsNumber + ", price=" + price + "]";
	}	

}
