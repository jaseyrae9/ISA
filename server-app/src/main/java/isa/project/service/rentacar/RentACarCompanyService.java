package isa.project.service.rentacar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.shared.DailyReportDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.dto.shared.WeeklyReportDTO;
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

	public Page<RentACarCompany> findAll(Pageable page) {
		return rentACarRepository.findAll(page);
	}

	public Iterable<RentACarCompany> findAll() {
		return rentACarRepository.findAll();
	}

	public Optional<RentACarCompany> findRentACarCompany(Integer id) {
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

		Car car = new Car(carCompany.get(), carDTO.getBrand(), carDTO.getModel(), carDTO.getYearOfProduction(),
				carDTO.getSeatsNumber(), carDTO.getDoorsNumber(), carDTO.getPrice(), carDTO.getType());
		return carRepository.save(car);
	}

	public BranchOffice addBranchOffice(Integer companyId, BranchOfficeDTO branchOfficeDTO)
			throws ResourceNotFoundException {
		Optional<RentACarCompany> carCompany = rentACarRepository.findById(companyId);

		if (!carCompany.isPresent()) {
			throw new ResourceNotFoundException(companyId.toString(), "Rent a car company not found");
		}

		BranchOffice branchOffice = new BranchOffice(carCompany.get(), branchOfficeDTO.getName());
		branchOffice.setLocation(branchOfficeDTO.getLocation());

		return branchOfficeRepository.save(branchOffice);
	}

	public Iterable<RentACarCompany> searchAll(String carCompanyName, String carCompanyAddress, String pickUp,
			String dropOff) throws ParseException {
		List<RentACarCompany> ret = new ArrayList<>();
		Iterable<RentACarCompany> companies = rentACarRepository.searchNameAndAddress(carCompanyName.toLowerCase(),
				carCompanyAddress.toLowerCase());

		for (RentACarCompany company : companies) {
			boolean free = true;

			RentACarCompany temp = rentACarRepository.findById(company.getId()).get();

			if (!pickUp.isEmpty() && !dropOff.isEmpty()) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				Date date0 = sdf1.parse(pickUp);
				Date date1 = sdf1.parse(dropOff);
				outerloop: for (Car car : temp.getCars()) {
					for (CarReservation cr : car.getCarReservations()) {
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

				if (free) {
					ret.add(temp);
				}
			} else {
				ret.add(temp);
			}

		}

		return ret;
	}

	public MonthlyReportDTO getRentACarMonthlyInfo(RentACarCompany rentACarCompany) {
		MonthlyReportDTO mrdto = new MonthlyReportDTO();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int year = cal.get(Calendar.YEAR);

		for (Car car : rentACarCompany.getCars()) {
			for (CarReservation reservation : car.getCarReservations()) {
				if (reservation.getActive()) {
					cal.setTime(reservation.getPickUpDate());
					int pickUpYear = cal.get(Calendar.YEAR);
					int pickUpMonth = cal.get(Calendar.MONTH);

					cal.setTime(reservation.getDropOffDate());
					int dropOffYear = cal.get(Calendar.YEAR);
					int dropOffMonth = cal.get(Calendar.MONTH);

					// prikazuju se samo rezervacije od trenutne godine
					if (pickUpYear == year && dropOffYear == year) {
						for (int i = pickUpMonth; i <= dropOffMonth; i++) {
							mrdto.increaseMonthly(i, car.getCarReservations().size());
						}
					}
				}
			}
		}

		return mrdto;
	}

	public WeeklyReportDTO getRentACarCompanyWeeklyInfo(RentACarCompany rentACarCompany) {
		WeeklyReportDTO wrdto = new WeeklyReportDTO();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int year = cal.get(Calendar.YEAR);

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int weekBegin = cal.get(Calendar.WEEK_OF_YEAR);
		int maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int weekEnd = weekBegin + maxWeekNumber - 1;

		for (Car car : rentACarCompany.getCars()) {
			for (CarReservation reservation : car.getCarReservations()) {
				if (reservation.getActive()) {
					cal.setTime(reservation.getPickUpDate());
					int pickUpYear = cal.get(Calendar.YEAR);
					int pickUpWeek = cal.get(Calendar.WEEK_OF_YEAR);
					cal.setTime(reservation.getDropOffDate());
					int dropOffYear = cal.get(Calendar.YEAR);
					int dropOffWeek = cal.get(Calendar.WEEK_OF_YEAR);

					if (pickUpYear == year && dropOffYear == year) {
						for (int i = pickUpWeek; i <= dropOffWeek; i++) {
							if (i <= weekEnd && i >= weekBegin) {
								wrdto.increaseWeekly(i - weekBegin, car.getCarReservations().size());
							}
						}
					}
				}
			}
		}
		return wrdto;
	}

	public DailyReportDTO getRentACompanyDailyInfo(RentACarCompany rentACarCompany) {
		DailyReportDTO drdto = new DailyReportDTO();

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_YEAR);

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		// Sunday = 1, Monday = 2, Thu = 3, Wed = 4, Thr = 5, Fri = 6, Sat = 7

		if (dayOfWeek != Calendar.SUNDAY) {
			dayOfWeek -= 2; // Monday = 0, Thu = 1,Wed = 2, Thr = 3, Fri = 4, Sat = 5
		} else {
			dayOfWeek = 7;
		}

		int firstDayOfWeekInYear = day - dayOfWeek;

		for (Car car : rentACarCompany.getCars()) {
			for (CarReservation reservation : car.getCarReservations()) {
				if (reservation.getActive()) {
					cal.setTime(reservation.getPickUpDate());
					int pickUpYear = cal.get(Calendar.YEAR);
					int pickUpDay = cal.get(Calendar.DAY_OF_YEAR);
					cal.setTime(reservation.getDropOffDate());
					int dropOffYear = cal.get(Calendar.YEAR);
					int dropOffDay = cal.get(Calendar.DAY_OF_YEAR);

					if (pickUpYear == year && dropOffYear == year) {
						for (int i = pickUpDay; i <= dropOffDay; i++) {
							if (i >= firstDayOfWeekInYear && i <= firstDayOfWeekInYear + 6) {
								drdto.increaseDaily(i - firstDayOfWeekInYear, car.getCarReservations().size());
							}
						}
					}
				}
			}
		}
		return drdto;
	}

	public Double getIncome(RentACarCompany rentACarCompany, String startDate, String endDate) throws ParseException {
		Double retVal = new Double(0);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

		Date start = sdf1.parse(startDate);
		java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());
		
		Date end = sdf1.parse(endDate);
		java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());
		
		for (Car car : rentACarCompany.getCars()) {
			for (CarReservation reservation : car.getCarReservations()) {
				System.out.println("Ovde2");
				if (reservation.getActive() && reservation.getDropOffDate().compareTo(sqlStartDate) > 0
						&& reservation.getDropOffDate().compareTo(sqlEndDate) < 0) {
					long diff = reservation.getDropOffDate().getTime() - reservation.getPickUpDate().getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
					retVal += days * car.getPrice();
				}
			}
		}

		return retVal;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public BranchOffice editBranchOffice(RentACarCompany rentACarCompany, BranchOfficeDTO branchOfficeDTO) {
		for (BranchOffice bo : rentACarCompany.getBranchOffices()) {
			if (bo.getId().equals(branchOfficeDTO.getId())) {
				bo.setName(branchOfficeDTO.getName());
				bo.getLocation().setAddress(branchOfficeDTO.getLocation().getAddress());
				bo.getLocation().setLat(branchOfficeDTO.getLocation().getLat());
				bo.getLocation().setLon(branchOfficeDTO.getLocation().getLon());
				return bo;
			}
		}
		return null;
	}

}
