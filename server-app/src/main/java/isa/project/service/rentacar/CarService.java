package isa.project.service.rentacar;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.model.rentacar.Car;
import isa.project.repository.rentacar.CarRepository;

@Service
public class CarService {
	
	@Autowired
	private CarRepository carRepository;

	public Optional<Car> findCar(Integer id) {
		return carRepository.findById(id);
	}
	
	public List<Car> findAll() {
		return carRepository.findAll();
	}
	
	public Car saveCar(Car car) {
		return carRepository.save(car);
	}
	
	public Iterable<Car> findAllFast(Integer id)
	{
		return carRepository.findFast(id);
	}
}
