package isa.project.service.hotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.dto.hotel.RoomDTO;
import isa.project.dto.shared.DailyReportDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.dto.shared.WeeklyReportDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.shared.AdditionalServiceRepository;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private AdditionalServiceRepository additionalServiceRepository;

	public Iterable<Hotel> findAll() {
		return hotelRepository.findAll();
	}

	public Page<Hotel> findAll(Pageable page) {
		return hotelRepository.findAll(page);
	}

	public Optional<Hotel> findHotel(Integer id) {
		return hotelRepository.findById(id);
	}

	public Hotel saveHotel(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	public AdditionalService saveService(AdditionalService as) {
		return additionalServiceRepository.save(as);
	}

	public Set<AdditionalService> findAllAdditionalServices(Integer id) {
		Optional<Hotel> h = findHotel(id);
		
		Set<AdditionalService> as = null;
		
		if(h.isPresent()) {
			as = h.get().getAdditionalServices();
		}
		 
		return as;
	}

	/**
	 * Dodaje novu dodatnu uslugu u hotel.
	 * 
	 * @param additionalService - informacije o dodatnoj usluzi
	 * @param id                - id hotela kojoj se dodaje dodatna usluga
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe hotel
	 */
	public AdditionalService addAdditionalService(AdditionalService additionalService, Integer id)
			throws ResourceNotFoundException {
		// pronadji hotel u koji se dodaje dodatna usluga
		Optional<Hotel> hotelOpt = hotelRepository.findById(id);
		if (!hotelOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found.");
		}

		Hotel hotel = hotelOpt.get();

		// sacuvaj dodatnu uslugu
		additionalService.setActive(true);
		AdditionalService service = additionalServiceRepository.save(additionalService);

		// sacuvaj informaciju o dodatnoj usluzi u hotelu
		hotel.addAdditionalService(additionalService);
		hotelRepository.save(hotel);

		return service;
	}

	public Room addRoom(Integer hotelId, RoomDTO roomDTO) throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelRepository.findById(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Room room = new Room(hotel.get(), roomDTO.getFloor(), roomDTO.getRoomNumber(), roomDTO.getNumberOfBeds(),
				roomDTO.getPrice(), roomDTO.getType());
		room.setVersion(new Long(0));
		return roomRepository.save(room);
	}

	public Iterable<Hotel> findSearchAll(String hotelName, String hotelAddress, String hotelCheckInDate,
			String hotelCheckOutDate) throws ParseException {

		List<Hotel> ret = new ArrayList<Hotel>();
		Iterable<Hotel> hotels = hotelRepository.findSearchAll(hotelName.toLowerCase(), hotelAddress.toLowerCase());

		for (Hotel hotel : hotels) {
			Hotel hotel_temp = hotelRepository.findById(hotel.getId()).get();
			if (!hotelCheckOutDate.isEmpty() && !hotelCheckOutDate.isEmpty()) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

				Date inDate = sdf1.parse(hotelCheckInDate);
				java.sql.Date sqlInDate = new java.sql.Date(inDate.getTime());

				Date outDate = sdf1.parse(hotelCheckOutDate);
				java.sql.Date sqlOutDate = new java.sql.Date(outDate.getTime());

				boolean free = true;
				outerloop: for (Room r : hotel_temp.getRooms()) {
					for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
						if (sqlInDate.compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
								&& sqlInDate.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
							free = false;
							break outerloop;
						}
						if (sqlOutDate.compareTo(srr.getRoomReservation().getCheckInDate()) >= 0
								&& sqlOutDate.compareTo(srr.getRoomReservation().getCheckOutDate()) <= 0) {
							free = false;
							break outerloop;
						}
					}
				}
				if (free) {
					ret.add(hotel_temp);
				}
			} else {
				ret.add(hotel_temp);
			}

		}
		return ret;
	}

	public MonthlyReportDTO getHotelMonthlyInfo(Hotel hotel) {
		MonthlyReportDTO midto = new MonthlyReportDTO();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int year = cal.get(Calendar.YEAR);

		for (Room r : hotel.getRooms()) {
			for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
				RoomReservation rr = srr.getRoomReservation();

				if (rr.getActive()) { // Ako rezervacija nije otkazana

					cal.setTime(rr.getCheckOutDate());
					int checkOutYear = cal.get(Calendar.YEAR);
					int checkOutMonth = cal.get(Calendar.MONTH);
					cal.setTime(rr.getCheckInDate());
					int checkInYear = cal.get(Calendar.YEAR);
					int checkInMonth = cal.get(Calendar.MONTH);

					if (checkOutYear == year && checkInYear == year) // Za sve rezervacije u ovoj godini
					{
						for (int i = checkInMonth; i <= checkOutMonth; i++) {
							midto.increaseMonthly(i, r.getNumberOfBeds());
						}
					}
				}
			}

		}

		return midto;
	}

	public WeeklyReportDTO getHotelWeeklyInfo(Hotel hotel) {
		WeeklyReportDTO widto = new WeeklyReportDTO();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int year = cal.get(Calendar.YEAR); //

		cal.set(Calendar.DAY_OF_MONTH, 1);
		int weekBegin = cal.get(Calendar.WEEK_OF_YEAR);
		int maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		int weekEnd = weekBegin + maxWeekNumber - 1;

		for (Room r : hotel.getRooms()) {
			for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
				RoomReservation rr = srr.getRoomReservation();

				if (rr.getActive()) { // Ako rezervacija nije otkazana

					cal.setTime(rr.getCheckOutDate());
					int checkOutYear = cal.get(Calendar.YEAR);
					int checkOutWeek = cal.get(Calendar.WEEK_OF_YEAR);
					cal.setTime(rr.getCheckInDate());
					int checkInYear = cal.get(Calendar.YEAR);
					int checkInWeek = cal.get(Calendar.WEEK_OF_YEAR);

					if (checkOutYear == year && checkInYear == year) // Za sve rezervacije u ovoj godini
					{
						for (int i = checkInWeek; i <= checkOutWeek; i++) {
							if (i >= weekBegin && i <= weekEnd) {
								widto.increaseWeekly(i - weekBegin, r.getNumberOfBeds());
							}
						}
					}
				}
			}

		}

		return widto;
	}

	public DailyReportDTO getHotelDailyInfo(Hotel hotel) {
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

		for (Room r : hotel.getRooms()) {
			for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
				RoomReservation rr = srr.getRoomReservation();

				if (rr.getActive()) { // Ako rezervacija nije otkazana

					cal.setTime(rr.getCheckOutDate());
					int checkOutYear = cal.get(Calendar.YEAR);
					int checkOutDay = cal.get(Calendar.DAY_OF_YEAR);
					cal.setTime(rr.getCheckInDate());
					int checkInYear = cal.get(Calendar.YEAR);
					int checkInDay = cal.get(Calendar.DAY_OF_YEAR);

					if (checkOutYear == year && checkInYear == year) // Za sve rezervacije u ovoj godini
					{
						for (int i = checkInDay; i <= checkOutDay; i++) {
							if (i >= firstDayOfWeekInYear && i <= firstDayOfWeekInYear + 6) {
								drdto.increaseDaily(i - firstDayOfWeekInYear, r.getNumberOfBeds());
							}
						}
					}
				}
			}
		}

		return drdto;
	}

	public Double getIncome(Hotel hotel, String startDate, String endDate) throws ParseException {
		Double retVal = new Double(0);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

		Date start = sdf1.parse(startDate);
		java.sql.Date sqlStartDate = new java.sql.Date(start.getTime());

		Date end = sdf1.parse(endDate);
		java.sql.Date sqlEndDate = new java.sql.Date(end.getTime());

		for (Room r : hotel.getRooms()) {
			for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
				RoomReservation rr = srr.getRoomReservation();
				if (rr.getActive() && rr.getCheckOutDate().compareTo(sqlStartDate) > 0
						&& rr.getCheckOutDate().compareTo(sqlEndDate) < 0) {
					// Politika mog hotela je da se placa kada se napusta soba
					long diff = rr.getCheckOutDate().getTime() - rr.getCheckInDate().getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
					retVal += days * r.getPrice();
				}
				for (AdditionalService s : rr.getAdditionalServices()) {
					retVal += s.getPrice();
				}

			}
		}

		return retVal;
	}
}
