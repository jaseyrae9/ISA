package isa.project.service.rentacar;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.reservations.CarReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.repository.rentacar.BranchOfficeRepository;
import isa.project.repository.rentacar.CarRepository;
import isa.project.repository.rentacar.CarReservationRepository;

@Service
public class CarReservationService {
	
	@Autowired
	private CarReservationRepository carReservationRepository;
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private BranchOfficeRepository branchOfficeRepository;
	
	public Optional<CarReservation> findCarReservation(Integer id) {
		return carReservationRepository.findById(id);
	}
	
	public CarReservation saveCarReservation(CarReservation carReservation) {
		return carReservationRepository.save(carReservation);
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
	@Lock(LockModeType.PESSIMISTIC_READ)
	public CarReservation reseve(CarReservationRequestDTO carReservationRequest) throws RequestDataException, ResourceNotFoundException {
		if (carReservationRequest.getPickUpDate().compareTo(carReservationRequest.getDropOffDate()) > 0) {
			throw new RequestDataException("Pick up date can not be after drop off date.");
		}
		
		Optional<Car> car = carRepository.findById(carReservationRequest.getCarId());
		if (!car.isPresent()) {
			throw new ResourceNotFoundException(carReservationRequest.getCarId().toString(), "Car is not foundy");
		}

		Optional<BranchOffice> bopu = branchOfficeRepository.findById(carReservationRequest.getPickUpBranchOffice());
		
		if (!bopu.isPresent()) {
			throw new ResourceNotFoundException(carReservationRequest.getPickUpBranchOffice().toString(), "Pick up location is not found");
		}
				
		Optional<BranchOffice> bodo = branchOfficeRepository.findById(carReservationRequest.getDropOffBranchOffice());

		if (!bodo.isPresent()) {
			throw new ResourceNotFoundException(carReservationRequest.getDropOffBranchOffice().toString(), "Drop off location is not found");
		}

		if(bodo.get().getRentACarCompany().getId() != bopu.get().getRentACarCompany().getId()) {
			throw new RequestDataException("Pick up branch office and drop off branch office are not in same company");
		}
		
		if(car.get().getRentACarCompany().getId() != bodo.get().getRentACarCompany().getId()) {
			throw new RequestDataException("Pick up branch office and drop off branch office are not from that company");
		}
		
		boolean free = true;
		for (CarReservation cr : car.get().getCarReservations()) {
			if (cr.getActive()) {
				if (carReservationRequest.getPickUpDate().compareTo(cr.getPickUpDate()) >= 0 
						&& carReservationRequest.getPickUpDate().compareTo(cr.getDropOffDate()) <= 0) {
					free = false;
					break;
				}
				if (carReservationRequest.getDropOffDate().compareTo(cr.getPickUpDate()) >= 0 
						&& carReservationRequest.getDropOffDate().compareTo(cr.getDropOffDate()) <= 0) {
					free = false;
					break;
				}
			}
			
		}
		if(!free) {
			throw new RequestDataException("Car is rented in that period.");
		}
		
		// Proveriti da li je mozda na brzoj rezervaciji
		if(car.get().getIsFast() && !carReservationRequest.getIsFastReservation()) {
			if (carReservationRequest.getPickUpDate().compareTo(car.get().getBeginDate()) >= 0 
					&& carReservationRequest.getPickUpDate().compareTo(car.get().getEndDate()) <= 0) {
				throw new RequestDataException("Car is rented in that period.");
			}
			if (carReservationRequest.getDropOffDate().compareTo(car.get().getBeginDate()) >= 0 
					&& carReservationRequest.getDropOffDate().compareTo(car.get().getEndDate()) <= 0) {
				throw new RequestDataException("Car is rented in that period.");
			}
		}

		CarReservation carReservation = new CarReservation(car.get(), carReservationRequest.getPickUpDate(),
				carReservationRequest.getDropOffDate(), bopu.get(), bodo.get());
		
		System.out.println("carReservation" + carReservation);
		return carReservationRepository.save(carReservation);
	}
	

}
