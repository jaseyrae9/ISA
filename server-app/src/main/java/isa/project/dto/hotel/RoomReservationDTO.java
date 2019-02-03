package isa.project.dto.hotel;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;

public class RoomReservationDTO {

	private Integer id;
	
	@NotNull (message = "Check in date can not be blank.")
	private Date checkInDate;
	
	@NotNull (message = "Check out date can not be blank.")
	private Date checkOutDate;
	
	private Set<AdditionalService> additionalServices = new HashSet<>();
	
	private Set<Room> reservations = new HashSet<>();
	
	private Set<Room> roomReservations = new HashSet<>();
	
	private Boolean active;
	
	private HotelDTO hotel;
	
	private Boolean isHotelRated;
	
	public RoomReservationDTO() {
		
	}	
	
	public RoomReservationDTO(RoomReservation roomReservation) {
		this.id = roomReservation.getId();
		this.checkInDate = roomReservation.getCheckInDate();
		this.checkOutDate = roomReservation.getCheckOutDate();
		this.active = roomReservation.getActive();
		this.additionalServices = roomReservation.getAdditionalServices();
		this.reservations = new HashSet<>();
		this.isHotelRated = roomReservation.getIsHotelRated();
		if(roomReservation.getSingleRoomReservations() != null) {
			for(SingleRoomReservation srr: roomReservation.getSingleRoomReservations()) {
				this.reservations.add(srr.getRoom());
			}
			// Da ga nateram da soba ima samo rezervacije koje se odnose na tu rezervaciju
			for(SingleRoomReservation srr: roomReservation.getSingleRoomReservations()) {
				System.out.println("SingleRoomReservation: " + srr.getId());
				Room room = new Room(srr.getRoom());
				
				Set<SingleRoomReservation> srrSet = new HashSet<>();
				for(SingleRoomReservation roomSingle : srr.getRoom().getSingleRoomReservations()) {
					System.out.println("SingleRoomReservationSoba: " + roomSingle.getId());
					if(roomSingle.getId().equals(srr.getId())) {
						System.out.println("dodata neka");
						srrSet.add(srr);
					}
					
				}
				
				room.setSingleRoomReservations(srrSet);
				this.roomReservations.add(room);
			}
		}
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Set<AdditionalService> getAdditionalServices() {
		return additionalServices;
	}

	public void setAdditionalServices(Set<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<Room> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Room> reservations) {
		this.reservations = reservations;
	}

	public HotelDTO getHotel() {
		return hotel;
	}

	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}

	public Set<Room> getRoomReservations() {
		return roomReservations;
	}

	public void setRoomReservations(Set<Room> roomReservations) {
		this.roomReservations = roomReservations;
	}

	public Boolean getIsHotelRated() {
		return isHotelRated;
	}

	public void setIsHotelRated(Boolean isHotelRated) {
		this.isHotelRated = isHotelRated;
	}
	
	
}
