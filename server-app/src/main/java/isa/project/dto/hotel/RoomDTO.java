package isa.project.dto.hotel;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import isa.project.model.hotel.Room;
import isa.project.model.hotel.SingleRoomReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RoomDTO {
	
	private Integer id;
	
	@NotNull(message = "Floor number can not be blank.")
	private Integer floor;
	
	@NotNull(message = "Room number can not be blank.")
	@Min(value = 0, message = "Room number can not be less than zero.")
	private Integer roomNumber;
	
	@NotNull(message = "Number of beds can not be blank.")
	@Min(value = 1, message = "There must be at least one bed.")
	private Integer numberOfBeds;
	
	@NotNull(message = "Price must be entered.")
	@Min(value = 0, message = "Price can not be less than zero.")
	private Double price;
	
	private String type;
	private Boolean active;	
	
	private Double averageRating;	
	
	private Boolean isFast;
	private Date startDate;
	private Date endDate;
	private Double discount;
	
	private ArrayList<SingleRoomReservationDTO> reservations = new ArrayList<>();
	
	public RoomDTO(Room room)
	{
		this.id = room.getId();
		this.floor = room.getFloor();
		this.roomNumber = room.getRoomNumber();
		this.numberOfBeds = room.getNumberOfBeds();
		this.price = room.getPrice();
		this.type = room.getType();
		this.active = room.getActive();
		this.averageRating = (room.getTotalRating() / (double) room.getRatingCount());
		if(room.getSingleRoomReservations() != null) {
			for(SingleRoomReservation srr: room.getSingleRoomReservations()) {
				this.reservations.add(new SingleRoomReservationDTO(srr));
			}
		}
		this.isFast = room.getIsFast();
		this.startDate = room.getBeginDate();
		this.endDate = room.getEndDate();
		this.discount = room.getDiscount();
	}
}
