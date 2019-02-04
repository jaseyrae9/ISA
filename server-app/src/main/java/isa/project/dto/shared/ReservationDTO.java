package isa.project.dto.shared;

import java.util.Date;
import java.util.Set;

import isa.project.dto.hotel.HotelDTO;
import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.rentacar.CarReservationDTO;
import isa.project.dto.rentacar.RentACarCompanyDTO;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.users.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReservationDTO {

	private Integer id;	
	private Date creationDate;	
	private CarReservationDTO carReservation;	
	private RoomReservationDTO roomReservation;
	private FlightReservation flightReservation;

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
		
		this.flightReservation = reservation.getFlightReservation();
	}	
}
