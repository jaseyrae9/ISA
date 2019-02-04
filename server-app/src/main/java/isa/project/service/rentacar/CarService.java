package isa.project.service.rentacar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.model.users.User;
import isa.project.repository.rentacar.CarRepository;
import isa.project.repository.rentacar.CarReservationRepository;
import isa.project.repository.rentacar.RentACarCompanyRepository;
import isa.project.repository.users.UserRepository;

@Service
public class CarService {

	@Autowired
	private RentACarCompanyRepository rentACarCompanyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarReservationRepository carReservationRepository;
	
	@Autowired
	private CarRepository carRepository;

	public Optional<Car> findCar(Integer id) {
		return carRepository.findById(id);
	}
	
	public Car saveCar(Car car) {
		return carRepository.save(car);
	}
	
	public Iterable<Car> findAllFast(Integer id)
	{
		return carRepository.findFast(id);
	}
	
	
	public CarReservation addReservation(Integer carCompanyId, Integer carId, String customer,
			Integer pickUpBranchOfficeId, Integer dropOffBranchOfficeId, String pickUpDate, String dropOffDate)
			throws ResourceNotFoundException, ParseException, RequestDataException {
		
		Optional<RentACarCompany> rentACarComapny = rentACarCompanyRepository.findById(carCompanyId);
		if (!rentACarComapny.isPresent()) {
			throw new ResourceNotFoundException(carCompanyId.toString(), "Car company is not found");
		}

		Optional<Car> car = rentACarComapny.get().getCars().stream().filter(o -> o.getId().equals(carId)).findFirst();
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carId.toString(), "Car is not found in that company");
		}

		Optional<User> user = userRepository.findByEmail(customer);
		
		if (!user.isPresent()) {
			throw new ResourceNotFoundException(customer, "Customer is not found");
		}

		Optional<BranchOffice> bopu = rentACarComapny.get().getBranchOffices().stream().filter(o -> o.getId().equals(pickUpBranchOfficeId))
				.findFirst();
		Optional<BranchOffice> bodo = rentACarComapny.get().getBranchOffices().stream().filter(o -> o.getId().equals(dropOffBranchOfficeId))
				.findFirst();

		if (!bopu.isPresent()) {
			throw new ResourceNotFoundException(pickUpBranchOfficeId.toString(),
					"Pick up location is not found in that company");
		}

		if (!bodo.isPresent()) {
			throw new ResourceNotFoundException(dropOffBranchOfficeId.toString(),
					"Drop off location is not found in that company");
		}

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date0 = sdf1.parse(pickUpDate);
		Date date1 = sdf1.parse(dropOffDate);
		java.sql.Date sqlPickUpDate = new java.sql.Date(date0.getTime());
		java.sql.Date sqlDropOffDate = new java.sql.Date(date1.getTime());

		if (date0.compareTo(date1) > 0) {
			throw new RequestDataException("Pick up date can not be after drop off date.");
		}

		boolean free = true;

		for (CarReservation cr : car.get().getCarReservations()) {
			if (date0.compareTo(cr.getPickUpDate()) >= 0 && date0.compareTo(cr.getDropOffDate()) <= 0) {
				free = false;
				break;
			}
			if (date1.compareTo(cr.getPickUpDate()) >= 0 && date1.compareTo(cr.getDropOffDate()) <= 0) {
				free = false;
				break;
			}
		}
		if(!free) {
			throw new RequestDataException("Car is rented in that period.");
		}

		CarReservation carReservation = new CarReservation(car.get(), sqlPickUpDate,
				sqlDropOffDate, bopu.get(), bodo.get());
		System.out.println("carReservation" + carReservation);

		return carReservationRepository.save(carReservation);
	}
}
