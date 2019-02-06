package isa.project.dto.rentacar;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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
	
	private Boolean isFast;
	
	private Date beginDate;
	
	private Date endDate;
	
	private Double discount;
	
	private ArrayList<CarReservationDTO> reservations = new ArrayList<>();
	private RentACarCompanyDTO rentACarCompany;
	
	private BranchOfficeDTO fastPickUpBranchOffice;
	private BranchOfficeDTO fastDropOffBranchOffice;
	
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
		this.isFast = car.getIsFast();
		this.beginDate = car.getBeginDate();
		this.endDate = car.getEndDate();
		this.discount = car.getDiscount();
		this.averageRating = (car.getTotalRating() / (double) car.getRatingCount());
		this.reservations = new ArrayList<>();
		if(car.getCarReservations() != null) {
			for(CarReservation cr: car.getCarReservations()) {
				this.reservations.add(new CarReservationDTO(cr));
			}
		}
		this.fastPickUpBranchOffice = new BranchOfficeDTO();
		this.fastPickUpBranchOffice.setId(car.getFastPickUpBranchOffice());
		
		this.fastDropOffBranchOffice = new BranchOfficeDTO();
		this.fastDropOffBranchOffice.setId(car.getFastDropOffBranchOffice());
	}
}
