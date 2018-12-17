package isa.project.dto.hotel;

import isa.project.model.hotel.Hotel;

public class HotelDTO {
	
	private Integer id;
	private String name;
	private String description;
	
	public HotelDTO() {
		
	}

	public HotelDTO(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.description = hotel.getDescription();
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
	
}
