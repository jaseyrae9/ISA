package isa.project.service.rentacar;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.rentacar.CarReservationDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.users.Customer;
import isa.project.model.users.User;
import isa.project.repository.rentacar.BranchOfficeRepository;
import isa.project.repository.rentacar.CarRepository;
import isa.project.repository.rentacar.CarReservationRepository;
import isa.project.repository.users.UserRepository;

@Service
public class CarService {


	@Autowired
	private CarRepository carRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CarReservationRepository carReservationRepository;
	
	@Autowired
	private BranchOfficeRepository branchOfficeRepository;

	public CarReservation addReservation(Integer carId, String customer, Integer pickUpBranchOfficeId,
			Integer dropOffBranchOfficeId, String pickUpDate, String dropOffDate) throws ResourceNotFoundException, ParseException {
		Optional<Car> car = carRepository.findById(carId);
		Optional<User> user = userRepository.findByEmail(customer);
		
		Optional<BranchOffice> bopu = branchOfficeRepository.findById(pickUpBranchOfficeId);
		Optional<BranchOffice> bodo = branchOfficeRepository.findById(dropOffBranchOfficeId);
		
		
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car is not found");
		}
		
		if (!user.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Customer is not found");
		}
		
		if (!bopu.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Pick up location not found");
		}
		
		if (!bodo.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Drop off location is not found");
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date0 = sdf1.parse(pickUpDate);
		java.util.Date date1 = sdf1.parse(dropOffDate);
		java.sql.Date sqlPickUpDate = new java.sql.Date(date0.getTime());  
		java.sql.Date sqlDropOffDate = new java.sql.Date(date1.getTime());  
		
		CarReservation carReservation = new CarReservation((Customer)user.get(), car.get(), sqlPickUpDate,
				sqlDropOffDate, bopu.get(), bodo.get());
		System.out.println("carReservation" + carReservation);
		
		return carReservationRepository.save(carReservation);
	}
}
