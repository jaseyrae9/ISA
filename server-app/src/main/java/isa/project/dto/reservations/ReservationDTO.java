package isa.project.dto.reservations;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
			
			long diff = reservation.getCarReservation().getDropOffDate().getTime() - reservation.getCarReservation().getPickUpDate().getTime();
			long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
			this.carReservation.setTotal(days * carDTO.getPrice());
			
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
