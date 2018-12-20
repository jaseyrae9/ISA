package isa.project.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import isa.project.model.hotel.Hotel;

@CrossOrigin(origins="http://localhost:4200")
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

}
