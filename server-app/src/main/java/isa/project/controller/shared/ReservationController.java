package isa.project.controller.shared;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.aircompany.AirCompanyService;
import isa.project.service.hotel.HotelService;
import isa.project.service.hotel.RoomReservationService;
import isa.project.service.hotel.RoomService;
import isa.project.service.rentacar.CarReservationService;
import isa.project.service.rentacar.CarService;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value="reservation")
public class ReservationController {

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RoomReservationService roomReservationService;
		
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private RentACarCompanyService rentACarCompanyService;
	
	@Autowired
	private CarReservationService carReservationService;
		
	@Autowired
	private CarService carService;
	
	@Autowired
	private AirCompanyService airService;
	
//	@Autowired
//	private FlightReservationsService flightReservationsService;
	
	@RequestMapping(value="/rateHotel/{hotelId}/{reservationId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Double> rateHotel(@PathVariable Integer hotelId, @PathVariable Integer reservationId, @PathVariable Integer rate) throws ResourceNotFoundException{
		
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);
		
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}
		
		hotel.get().incrementRatingCount();
		hotel.get().addToTotalRating(rate);
		hotelService.saveHotel(hotel.get());
		
		Optional<RoomReservation> roomReservation = roomReservationService.findRoomReservation(reservationId);
		
		if (!roomReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		roomReservation.get().setIsHotelRated(true);
		roomReservationService.saveRoomReservation(roomReservation.get());
		
		return new ResponseEntity<>((hotel.get().getTotalRating() / (double) hotel.get().getRatingCount()), HttpStatus.OK);	
	}
	
	@RequestMapping(value="/rateRoom/{roomId}/{singleRoomReservationId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Double> rateRoom(@PathVariable Integer roomId, @PathVariable Integer singleRoomReservationId, @PathVariable Integer rate) throws ResourceNotFoundException {
		System.out.println("Ocenjivanje sobe: " + roomId);
	
		Optional<Room> room = roomService.findRoom(roomId);
		
		if (!room.isPresent()) {
			throw new ResourceNotFoundException(roomId.toString(), "Room not found");
		}
		
		room.get().incrementRatingCount();
		room.get().addToTotalRating(rate);
		roomService.saveRoom(room.get());
		
		Optional<SingleRoomReservation> singleRoomReservation = roomReservationService.findSingleRoomReservation(singleRoomReservationId);

		if (!singleRoomReservation.isPresent()) {
			throw new ResourceNotFoundException(singleRoomReservationId.toString(), "Reservation not found");
		}
		
		singleRoomReservation.get().setIsRoomRated(true);
		roomReservationService.saveSingleRoomResrvation(singleRoomReservation.get());
		
		return new ResponseEntity<>((room.get().getTotalRating() / (double) room.get().getRatingCount()), HttpStatus.OK);	
	}
	
	
	@RequestMapping(value="/rateCarCompany/{comapnyId}/{reservationId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> rateCarCompany(@PathVariable Integer comapnyId,@PathVariable Integer reservationId, @PathVariable Integer rate) throws ResourceNotFoundException{
		System.out.println("Ocenjivanje car comapany: " + comapnyId);
		
		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(comapnyId);
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(comapnyId.toString(), "Car company not found");
		}
		
		carCompany.get().incrementRatingCount();
		carCompany.get().addToTotalRating(rate);
		rentACarCompanyService.saveRentACarCompany(carCompany.get());
		
		Optional<CarReservation> carReservation = carReservationService.findCarReservation(reservationId);

		if (!carReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		carReservation.get().setIsCompanyRated(true);  
		carReservationService.saveCarReservation(carReservation.get()); 
		
		return new ResponseEntity<>((carCompany.get().getTotalRating() / (double) carCompany.get().getRatingCount()), HttpStatus.OK);	
	}
	
	@RequestMapping(value="/rateCar/{carId}/{reservationId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> rateCar(@PathVariable Integer carId,@PathVariable Integer reservationId, @PathVariable Integer rate) throws ResourceNotFoundException{
		System.out.println("Ocenjivanje automobila: " + carId);
		
		Optional<Car> car = carService.findCar(carId);
		
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car not found");
		}
		
		car.get().incrementRatingCount();
		car.get().addToTotalRating(rate);
		carService.saveCar(car.get());
		
		Optional<CarReservation> carReservation = carReservationService.findCarReservation(reservationId);

		if (!carReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		carReservation.get().setIsCarRated(true);
		carReservationService.saveCarReservation(carReservation.get()); 
		
		return new ResponseEntity<>((car.get().getTotalRating() / (double) car.get().getRatingCount()), HttpStatus.OK);	
	}
	
	@RequestMapping(value="/rateAirCompany/{comapanyId}/{reservationId}/{rate}",method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> rateAirCompany(@PathVariable Integer comapanyId, @PathVariable Integer reservationId, @PathVariable Integer rate) throws ResourceNotFoundException{
		System.out.println("Ocenjivanje air comapany: " + comapanyId);
		
		Optional<AirCompany> company = airService.findAircompany(comapanyId);
		
		if (!company.isPresent()) {
			throw new ResourceNotFoundException(comapanyId.toString(), "Air company not found");
		}
		
		company.get().incrementRatingCount();
		company.get().addToTotalRating(rate);
		airService.saveAirCompany(company.get());
		
//		Optional<FlightReservation> flightReservation = flightReservationsService.
//
//		if (!flightReservation.isPresent()) {
//			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
//		}
		
		//flightReservation.get().setIsCompanyRated(true);  
		//carReservationService.saveCarReservation(carReservation.get()); 
		
		//return new ResponseEntity<>((company.get().getTotalRating() / (double) company.get().getRatingCount()), HttpStatus.OK);	
		return null;
	}
	
	
}
