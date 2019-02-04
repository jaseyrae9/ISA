package isa.project.dto.rentacar;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.rentacar.BranchOffice;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BranchOfficeDTO {
	private Integer id;	
	@NotBlank (message = "Name can not be blank.")
	private String name;	
	private Boolean active;
	@NotNull (message = "Location must be enetered.")
	private Location location;

	public BranchOfficeDTO(BranchOffice branchOffice) {
		this.id = branchOffice.getId();
		this.name = branchOffice.getName();
		this.active = branchOffice.getActive();
		this.location = branchOffice.getLocation();

	}
}
