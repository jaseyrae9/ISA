package isa.project.service.rentacar;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
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
		
		BranchOffice branchOffice = new BranchOffice(
				carCompany.get(), branchOfficeDTO.getName());
		
		return branchOfficeRepository.save(branchOffice);
	}
	
}
