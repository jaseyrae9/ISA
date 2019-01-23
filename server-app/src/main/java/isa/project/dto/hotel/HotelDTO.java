package isa.project.dto.hotel;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;

public class HotelDTO {
	
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	
	private String description;
	
	private ArrayList<RoomDTO> rooms = new ArrayList<>();
	
	public HotelDTO() {
		
	}

	public HotelDTO(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.description = hotel.getDescription();
		this.rooms = new ArrayList<>();
		if(hotel.getRooms() != null) {
			for(Room room: hotel.getRooms()) {
				this.rooms.add(new RoomDTO(room));
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
	
}
