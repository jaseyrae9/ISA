package isa.project.dto.rentacar;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;

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
	@Max(value = 200, message = "Price can not be greater than 500.")
	private Double price;
	
	private Boolean active;
	
	private Double averageRating;
	
	private ArrayList<CarReservationDTO> reservations = new ArrayList<>();

	private RentACarCompanyDTO rentACarCompany;
	
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
		this.averageRating = (car.getTotalRating() / (double) car.getRatingCount());
		this.reservations = new ArrayList<>();
		if(car.getCarReservations() != null) {
			for(CarReservation cr: car.getCarReservations()) {
				this.reservations.add(new CarReservationDTO(cr));
			}
		}
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

	public ArrayList<CarReservationDTO> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<CarReservationDTO> reservations) {
		this.reservations = reservations;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public RentACarCompanyDTO getRentACarCompany() {
		return rentACarCompany;
	}

	public void setRentACarCompany(RentACarCompanyDTO rentACarCompany) {
		this.rentACarCompany = rentACarCompany;
	}
}
