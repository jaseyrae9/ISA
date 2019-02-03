package isa.project.service.rentacar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.rentacar.CarReservation;
import isa.project.repository.rentacar.CarReservationRepository;

@Service
public class CarReservationService {
	
	@Autowired
	private CarReservationRepository carReservationRepository;
	
	public Optional<CarReservation> findCarReservation(Integer id) {
		return carReservationRepository.findById(id);
	}
	
	public CarReservation saveCarReservation(CarReservation carReservation) {
		return carReservationRepository.save(carReservation);
	}

}
