package isa.project.dto.hotel;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.shared.AdditionalService;
import isa.project.model.shared.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HotelDTO {
	
	private Integer id;
	
	@NotBlank (message = "Name can not be blank.")
	private String name;
	
	private String description;
	
	private ArrayList<RoomDTO> rooms = new ArrayList<>();
	
	private ArrayList<AdditionalService> additionalServices = new ArrayList<>();
	
	@NotNull (message = "Location must be enetered.")
	private Location location;
	
	private Double averageRating;	
	

	public HotelDTO(Hotel hotel) {
		this.id = hotel.getId();
		this.name = hotel.getName();
		this.description = hotel.getDescription();
		this.location = hotel.getLocation();
		this.averageRating = (hotel.getTotalRating() / (double) hotel.getRatingCount());
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
	
}
