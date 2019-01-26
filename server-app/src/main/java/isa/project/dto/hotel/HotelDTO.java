package isa.project.dto.hotel;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;

public class HotelDTO {
	
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	
	private String description;
	
	private ArrayList<RoomDTO> rooms = new ArrayList<>();
	
	private ArrayList<AdditionalService> additionalServices = new ArrayList<>();
	
	@NotNull (message = "Location must be enetered.")
	private Location location;
	
	public HotelDTO() {
		
	}

	public HotelDTO(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.description = hotel.getDescription();
		this.location = hotel.getLocation();
		
		this.rooms = new ArrayList<>();
		if(hotel.getRooms() != null) {
			for(Room room: hotel.getRooms()) {
				this.rooms.add(new RoomDTO(room));
			}
		}
		
		this.additionalServices = new ArrayList<>();
		if(hotel.getAdditionalServices() != null) {
			for(AdditionalService ad: hotel.getAdditionalServices()) {
				this.additionalServices.add(ad);
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

	public ArrayList<RoomDTO> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<RoomDTO> rooms) {
		this.rooms = rooms;
	}

	public ArrayList<AdditionalService> getAdditionalServices() {
		return additionalServices;
	}

	public void setAdditionalServices(ArrayList<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}		
}
