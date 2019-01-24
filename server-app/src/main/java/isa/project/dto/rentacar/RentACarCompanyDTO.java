package isa.project.dto.rentacar;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.shared.Location;

public class RentACarCompanyDTO {
	
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	private String description;
	
	private ArrayList<CarDTO> cars = new ArrayList<>();
	private ArrayList<BranchOfficeDTO> branchOffices = new ArrayList<>();
	
	@NotNull (message = "Location must be enetered.")
	private Location location;
	
	public RentACarCompanyDTO() {
		
	}
	
	public RentACarCompanyDTO(RentACarCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.description = company.getDescription();
		this.location = company.getLocation();
		this.cars = new ArrayList<>();
		if(company.getCars() != null) {
			for(Car car: company.getCars()){
				this.cars.add(new CarDTO(car));
			}
		}
		this.branchOffices = new ArrayList<>();
		if(company.getBranchOffices() != null) {
			for(BranchOffice bo: company.getBranchOffices()) {
				this.branchOffices.add(new BranchOfficeDTO(bo));
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<CarDTO> getCars() {
		return cars;
	}

	public void setCars(ArrayList<CarDTO> cars) {
		this.cars = cars;
	}

	public ArrayList<BranchOfficeDTO> getBranchOffices() {
		return branchOffices;
	}

	public void setBranchOffices(ArrayList<BranchOfficeDTO> branchOffices) {
		this.branchOffices = branchOffices;
	}
		
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
