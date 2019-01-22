package isa.project.dto.rentacar;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.Car;

public class CarDTO {

	private Integer id;
	private String type;
	
	@NotBlank(message = "Car brand can not be blank.")
	private String brand;
	
	@NotBlank(message = "Car model can not be blank.")
	private String model;
	
	@Min(value = 0, message = "Year of production can not be less than zero.")
	private Integer yearOfProduction;
	
	@NotNull(message = "Number of seats can not be blank.")
	@Min(value = 1, message = "There must be at least one car seat.")
	private Integer seatsNumber;
	
	@NotNull(message = "Number of doors can not be blank.")
	@Min(value = 1, message = "There must be at least one car door.")
	private Integer doorsNumber;
	
	@NotNull(message = "Price must be entered.")
	@Min(value = 0, message = "Price can not be less than zero.")
	private Double price;
	
	private Boolean active;

	public CarDTO() {

	}

	public CarDTO(Car car) {
		this.id = car.getId();
		this.brand = car.getBrand();
		this.model = car.getModel();
		this.yearOfProduction = car.getYearOfProduction();
		this.seatsNumber = car.getSeatsNumber();
		this.doorsNumber = car.getDoorsNumber();
		this.price = car.getPrice();
		this.type = car.getType();
		this.active = car.getActive();
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
	
	
}
