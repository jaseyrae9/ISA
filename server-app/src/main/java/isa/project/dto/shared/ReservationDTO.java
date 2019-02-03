package isa.project.dto.shared;

import java.util.Date;
import java.util.Set;

import isa.project.dto.hotel.HotelDTO;
import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.rentacar.CarReservationDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.users.Reservation;

public class ReservationDTO {

	private Integer id;
	
	private Date creationDate;
	
	private CarReservationDTO carReservation;
	
	private RoomReservationDTO roomReservation;

	public ReservationDTO() {

	}

	public ReservationDTO(Reservation reservation) {
		this.id = reservation.getId();
		this.creationDate = reservation.getCreationDate();
		if(reservation.getCarReservation() != null) {
			this.carReservation = new CarReservationDTO(reservation.getCarReservation());
			CarDTO carDTO = new CarDTO(reservation.getCarReservation().getCar());
			carDTO.setRentACarCompany(new RentACarCompanyDTO(reservation.getCarReservation().getCar().getRentACarCompany()));
			this.carReservation.setCar(carDTO);
		}
		if(reservation.getRoomReservation() != null) {
			this.roomReservation = new RoomReservationDTO(reservation.getRoomReservation());
			Set<SingleRoomReservation> rooms = reservation.getRoomReservation().getSingleRoomReservations();
			if(rooms != null) {
				this.roomReservation.setHotel(new HotelDTO(rooms.iterator().next().getRoom().getHotel()));
			}
			
		}
		
	}

	public CarReservationDTO getCarReservation() {
		return carReservation;
	}

	public void setCarReservation(CarReservationDTO carReservation) {
		this.carReservation = carReservation;
	}

	public RoomReservationDTO getRoomReservation() {
		return roomReservation;
	}

	public void setRoomReservation(RoomReservationDTO roomReservation) {
		this.roomReservation = roomReservation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
