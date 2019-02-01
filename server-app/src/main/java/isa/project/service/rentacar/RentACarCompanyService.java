package isa.project.service.rentacar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.repository.rentacar.BranchOfficeRepository;
import isa.project.repository.rentacar.CarRepository;
import isa.project.repository.rentacar.RentACarCompanyRepository;

@Service
public class RentACarCompanyService {

	@Autowired
	private RentACarCompanyRepository rentACarRepository;
	
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private BranchOfficeRepository branchOfficeRepository;
	
	public Page<RentACarCompany> findAll(Pageable page){
		return rentACarRepository.findAll(page);
	}
	
	public Iterable<RentACarCompany> findAll(){
		return rentACarRepository.findAll();
	}
	
	public Optional<RentACarCompany> findRentACarCompany(Integer id){
		return rentACarRepository.findById(id);
	}
	
	public RentACarCompany saveRentACarCompany(RentACarCompany company) {
		return rentACarRepository.save(company);
	}

	public Car addCar(Integer companyId, CarDTO carDTO) throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarRepository.findById(companyId);
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}		
		
		Car car = new Car(carCompany.get(), carDTO.getBrand(), carDTO.getModel(), carDTO.getYearOfProduction(), carDTO.getSeatsNumber(), carDTO.getDoorsNumber(), carDTO.getPrice(), carDTO.getType());
		
		System.out.println("Cuvanje automobila");
		return carRepository.save(car);
	}
	
	public BranchOffice addBranchOffice(Integer companyId, BranchOfficeDTO branchOfficeDTO) throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarRepository.findById(companyId);
		
		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}
		
		BranchOffice branchOffice = new BranchOffice(carCompany.get(), branchOfficeDTO.getName());
		branchOffice.setLocation(branchOfficeDTO.getLocation());
		
		return branchOfficeRepository.save(branchOffice);
	}

	public Iterable<RentACarCompany> searchAll(String carCompanyName, String carCompanyAddress, String pickUp, String dropOff) throws ParseException {
		List<RentACarCompany> ret = new ArrayList<>();
		Iterable<RentACarCompany> companies = rentACarRepository.searchNameAndAddress(carCompanyName, carCompanyAddress);			
	
		for(RentACarCompany company: companies) {			
			boolean free = true;
			
			RentACarCompany temp = rentACarRepository.findById(company.getId()).get();
			
			if(!pickUp.equals("") && !dropOff.equals("")) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date0 = sdf1.parse(pickUp);
				java.util.Date date1 = sdf1.parse(dropOff);	
				outerloop:for(Car car: temp.getCars()) {
					for(CarReservation cr: car.getCarReservations()) {
						if (date0.compareTo(cr.getPickUpDate()) >= 0 && date0.compareTo(cr.getDropOffDate()) <= 0) {
							free = false;
							break outerloop;
						}
						if (date1.compareTo(cr.getPickUpDate()) >= 0 && date1.compareTo(cr.getDropOffDate()) <= 0) {
							free = false;
							break outerloop;
						}
					}
				}
				
				if(free) {
					ret.add(temp);
				}
			}
			else
			{
				ret.add(temp);
			}
			
		}
		
		return ret;
	}	
	
}
