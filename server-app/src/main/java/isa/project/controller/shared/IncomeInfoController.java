package isa.project.controller.shared;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.HotelAdminCheck;
import isa.project.dto.shared.DailyReportDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.dto.shared.WeeklyReportDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.service.hotel.HotelService;

@RestController
@RequestMapping(value="report_info")
public class IncomeInfoController {
	

	@Autowired
	private HotelService hotelService;
	
	
	
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

}
