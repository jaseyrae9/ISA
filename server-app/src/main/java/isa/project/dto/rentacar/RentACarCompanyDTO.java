package isa.project.dto.rentacar;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RentACarCompanyDTO {
	
	private Integer id;	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	private String description;	
	private ArrayList<CarDTO> cars = new ArrayList<>();
	private ArrayList<BranchOfficeDTO> branchOffices = new ArrayList<>();	
	@NotNull (message = "Location must be enetered.")
	private Location location;	
	private Double averageRating;
		
	public RentACarCompanyDTO(RentACarCompany company) {
		this.id = company.getId();
		this.name = company.getName();
		this.description = company.getDescription();
		this.location = company.getLocation();
		this.averageRating = (company.getTotalRating() / (double) company.getRatingCount());
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
}
