package isa.project.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.rentacar.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {

}
