package isa.project.repository.aircompany;

import org.springframework.data.jpa.repository.JpaRepository;

import isa.project.model.aircompany.Airplane;

public interface AirplaneRepository extends JpaRepository<Airplane, Integer> {

}
