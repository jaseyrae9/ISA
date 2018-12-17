package isa.project.repository.hotel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import isa.project.model.hotel.Hotel;

public interface HotelRepository extends CrudRepository<Hotel, Integer> {

}
