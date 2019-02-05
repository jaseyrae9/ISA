package isa.project.model.shared;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "services")
public class AdditionalService {
	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank(message = "Service name can not be blank.")
	@Column(nullable = false)
	private String name;
	
	@Column
	private String description;
	
	@Column
	private Boolean isFast;
	
	@Column
	private Boolean active;
	
	@NotNull(message = "Price must be entered.")
	@Min(value = 0, message = "Price can not be less than zero.")
	@Column(nullable = false)
	private Double price;
	
	public AdditionalService(String name, String description, Double price) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.active = true;
	}
}
