package isa.project.controller.shared;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Flight;
import isa.project.model.aircompany.FlightReservation;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.users.Customer;
import isa.project.repository.aircompany.FlightReservationRepository;
import isa.project.security.TokenUtils;
import isa.project.service.aircompany.AirCompanyService;
import isa.project.service.hotel.HotelService;
import isa.project.service.hotel.RoomReservationService;
import isa.project.service.hotel.RoomService;
import isa.project.service.rentacar.CarReservationService;
import isa.project.service.rentacar.CarService;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value = "reservation")
public class RatingController {

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

	@Autowired
	private FlightReservationRepository flightReservationRepository;
	
	@Autowired
	private TokenUtils tokenUtils;

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateHotel/{hotelId}/{reservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<Double> rateHotel(@PathVariable Integer hotelId, @PathVariable Integer reservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {

		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Optional<RoomReservation> roomReservation = roomReservationService.findRoomReservation(reservationId);

		if (!roomReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}

		if (!roomReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}
		
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));

		Customer c = roomReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}

		Date today = new Date();
		if (roomReservation.get().getCheckOutDate().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");

		}

		hotel.get().incrementRatingCount();
		hotel.get().addToTotalRating(rate);
		hotelService.saveHotel(hotel.get());

		roomReservation.get().setIsHotelRated(true);
		roomReservationService.saveRoomReservation(roomReservation.get());

		return new ResponseEntity<>((hotel.get().getTotalRating() / (double) hotel.get().getRatingCount()),
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateRoom/{roomId}/{singleRoomReservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<Double> rateRoom(@PathVariable Integer roomId, @PathVariable Integer singleRoomReservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {

		Optional<Room> room = roomService.findRoom(roomId);

		if (!room.isPresent()) {
			throw new ResourceNotFoundException(roomId.toString(), "Room not found");
		}

		Optional<SingleRoomReservation> singleRoomReservation = roomReservationService
				.findSingleRoomReservation(singleRoomReservationId);

		if (!singleRoomReservation.isPresent()) {
			throw new ResourceNotFoundException(singleRoomReservationId.toString(), "Reservation not found");
		}

		if (!singleRoomReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}
		
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));

		Customer c = singleRoomReservation.get().getRoomReservation().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}

		Date today = new Date();
		if (singleRoomReservation.get().getRoomReservation().getCheckOutDate().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");

		}

		room.get().incrementRatingCount();
		room.get().addToTotalRating(rate);
		roomService.saveRoom(room.get());

		singleRoomReservation.get().setIsRoomRated(true);
		roomReservationService.saveSingleRoomResrvation(singleRoomReservation.get());

		return new ResponseEntity<>((room.get().getTotalRating() / (double) room.get().getRatingCount()),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateCarCompany/{comapnyId}/{reservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<?> rateCarCompany(@PathVariable Integer comapnyId, @PathVariable Integer reservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {

		Optional<RentACarCompany> carCompany = rentACarCompanyService.findRentACarCompany(comapnyId);

		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(comapnyId.toString(), "Car company not found");
		}

		Optional<CarReservation> carReservation = carReservationService.findCarReservation(reservationId);

		if (!carReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		Customer c = carReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}

		if (!carReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}

		Date today = new Date();
		if (carReservation.get().getDropOffDate().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");
		}

		carCompany.get().incrementRatingCount();
		carCompany.get().addToTotalRating(rate);
		rentACarCompanyService.saveRentACarCompany(carCompany.get());

		carReservation.get().setIsCompanyRated(true);
		carReservationService.saveCarReservation(carReservation.get());

		return new ResponseEntity<>((carCompany.get().getTotalRating() / (double) carCompany.get().getRatingCount()),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateCar/{carId}/{reservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<?> rateCar(@PathVariable Integer carId, @PathVariable Integer reservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {

		Optional<Car> car = carService.findCar(carId);

		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car not found");
		}

		Optional<CarReservation> carReservation = carReservationService.findCarReservation(reservationId);

		if (!carReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}

		Customer c = carReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}
		
		if (!carReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}

		Date today = new Date();
		if (carReservation.get().getDropOffDate().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");
		}

		car.get().incrementRatingCount();
		car.get().addToTotalRating(rate);
		carService.saveCar(car.get());

		carReservation.get().setIsCarRated(true);
		carReservationService.saveCarReservation(carReservation.get());

		return new ResponseEntity<>((car.get().getTotalRating() / (double) car.get().getRatingCount()), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateAirCompany/{comapanyId}/{reservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<?> rateAirCompany(@PathVariable Integer comapanyId, @PathVariable Long reservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {

		Optional<AirCompany> company = airService.findAircompany(comapanyId);
		
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		
		if (!company.isPresent()) {
			throw new ResourceNotFoundException(comapanyId.toString(), "Air company not found");
		}

		Optional<FlightReservation> flightReservation = flightReservationRepository.findById(reservationId);

		if (!flightReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		Customer c = flightReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}

		if (!flightReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}
		
		Date today = new Date();
		if (flightReservation.get().getFlight().getEndDateAndTime().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");
		}
		
		company.get().incrementRatingCount();
		company.get().addToTotalRating(rate);
		airService.saveAirCompany(company.get());

		flightReservation.get().setIsCompanyRated(true);
		flightReservationRepository.save(flightReservation.get());

		return new ResponseEntity<>((company.get().getTotalRating() / (double) company.get().getRatingCount()), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rateFlight/{flightId}/{reservationId}/{rate}", method = RequestMethod.PUT)
	public ResponseEntity<?> rateFlight(@PathVariable Integer flightId, @PathVariable Long reservationId,
			@PathVariable Integer rate, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException {
		
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));

		Optional<FlightReservation> flightReservation = flightReservationRepository.findById(reservationId);

		if (!flightReservation.isPresent()) {
			throw new ResourceNotFoundException(reservationId.toString(), "Reservation not found");
		}
		
		Customer c = flightReservation.get().getReservation().getCustomer();
		
		if(!c.getEmail().equals(email)) {
			throw new ResourceNotFoundException(email.toString(), "You don't have this reservation.");
		}

		if (!flightReservation.get().getActive()) {
			throw new RequestDataException("You can't rate reservation you canceled.");
		}

		Date today = new Date();
		if (flightReservation.get().getFlight().getEndDateAndTime().compareTo(today) > 0) {
			throw new RequestDataException("You can't rate until your reservation has finished.");
		}
		
		Flight flight = flightReservation.get().getFlight();
		
		flight.incrementRatingCount();
		flight.addToTotalRating(rate);

		flightReservation.get().setIsFlightRated(true);
		flightReservationRepository.save(flightReservation.get());

		return new ResponseEntity<>((flight.getTotalRating() / (double) flight.getRatingCount()), HttpStatus.OK);
	}

}
