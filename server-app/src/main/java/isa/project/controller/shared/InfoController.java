package isa.project.controller.shared;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.HotelAdminCheck;
import isa.project.aspects.RentACarCompanyAdminCheck;
import isa.project.dto.shared.DailyReportDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.dto.shared.WeeklyReportDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.service.hotel.HotelService;
import isa.project.service.rentacar.RentACarCompanyService;

@RestController
@RequestMapping(value="report_info")
public class InfoController {
	

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RentACarCompanyService rentACarCompanyService;
	
	
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/hotelMonthly/{id}",method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<MonthlyReportDTO> getHotelMontly(@PathVariable Integer id) throws ResourceNotFoundException{
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(id);
		
		//hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		
		return new ResponseEntity<>(hotelService.getHotelMonthlyInfo(opt.get()), HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/hotelWeekly/{id}",method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<WeeklyReportDTO> getHotelWeekly(@PathVariable Integer id) throws ResourceNotFoundException{
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(id);
		
		//hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		
		return new ResponseEntity<>(hotelService.getHotelWeeklyInfo(opt.get()), HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/hotelDaily/{id}",method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<DailyReportDTO> getHotelDaily(@PathVariable Integer id) throws ResourceNotFoundException{
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(id);
		
		//hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		
		return new ResponseEntity<>(hotelService.getHotelDailyInfo(opt.get()), HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value="/hotelIncome/{id}/{startDate}/{endDate}",method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<Double> getHotelIncome(@PathVariable Integer id, @PathVariable String startDate, @PathVariable String endDate) throws ResourceNotFoundException, ParseException{
		//hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(id);
		
		//hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		
		return new ResponseEntity<>(hotelService.getIncome(opt.get(), startDate, endDate), HttpStatus.OK);	
	}

	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value="/rentACarCompanyMonthly/{id}", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<MonthlyReportDTO> getRentACarCompanyMontly(@PathVariable Integer id) throws ResourceNotFoundException{
		//rent-a-car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(id);
		
		//rent-a-car company is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car comapny not found");
		}
		
		return new ResponseEntity<>(rentACarCompanyService.getRentACarMonthlyInfo(opt.get()), HttpStatus.OK);	
	}
	
	
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value="/rentACarCompanyWeekly/{id}", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<WeeklyReportDTO> getRentACarCompanyWeekly(@PathVariable Integer id) throws ResourceNotFoundException{
		//rent-a-car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(id);
		
		//rent-a-car company is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car comapny not found");
		}
				
		return new ResponseEntity<>(rentACarCompanyService.getRentACarCompanyWeeklyInfo(opt.get()), HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value="/rentACarCompanyDaily/{id}", method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<DailyReportDTO> getRentACarCompanyDaily(@PathVariable Integer id) throws ResourceNotFoundException{
		//rent-a-car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(id);
		
		//rent-a-car company is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car comapny not found");
		}			
		
		return new ResponseEntity<>(rentACarCompanyService.getRentACompanyDailyInfo(opt.get()), HttpStatus.OK);	
	}
	
	@PreAuthorize("hasAnyRole('CARADMIN')")
	@AdminAccountActiveCheck
	@RentACarCompanyAdminCheck
	@RequestMapping(value="/rentACarCompanyIncome/{id}/{startDate}/{endDate}",method=RequestMethod.GET, consumes="application/json")
	public ResponseEntity<Double> getRentACarCompanyIncome(@PathVariable Integer id, @PathVariable String startDate, @PathVariable String endDate) throws ResourceNotFoundException, ParseException{
		//rent-a-car company must exist
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(id);
		
		//rent-a-car company is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Rent a car comapny not found");
		}	
		
		return new ResponseEntity<>(rentACarCompanyService.getIncome(opt.get(), startDate, endDate), HttpStatus.OK);	
	}
}
