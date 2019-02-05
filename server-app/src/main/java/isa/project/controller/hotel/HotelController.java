package isa.project.controller.hotel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.HotelAdminCheck;
import isa.project.dto.hotel.HotelDTO;
import isa.project.dto.hotel.RoomDTO;
import isa.project.dto.hotel.RoomReservationDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.hotel.RoomReservation;
import isa.project.model.hotel.SingleRoomReservation;
import isa.project.model.shared.AdditionalService;
import isa.project.service.hotel.HotelService;
import isa.project.service.hotel.RoomService;

@RestController
@RequestMapping(value = "hotels")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RoomService roomService;

	@RequestMapping(value = "/allHotels", method = RequestMethod.GET)
	public ResponseEntity<List<HotelDTO>> getAllHotels() {
		Iterable<Hotel> hotels = hotelService.findAll();

		List<HotelDTO> ret = new ArrayList<>();
		for (Hotel hotel : hotels) {
			ret.add(new HotelDTO(hotel));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	/**
	 * Returns DTO objects for hotels. Objects contain id, name, address and
	 * description.
	 * 
	 * @return information about all hotels.
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllHotelsPage(HttpServletRequest request, Pageable page) {
		Page<Hotel> hotels = hotelService.findAll(page);

		// convert hotels to DTO
		List<HotelDTO> hotelsDTO = new ArrayList<>();
		for (Hotel hotel : hotels) {
			hotelsDTO.add(new HotelDTO(hotel));
		}

		Page<HotelDTO> ret = new PageImpl<>(hotelsDTO, hotels.getPageable(), hotels.getTotalElements());
		return ResponseEntity.ok(ret);
	}

	/**
	 * Returns data about hotel with selected id.
	 * 
	 * @param id - id of hotel
	 * @return
	 * @throws ResourceNotFoundException - if there is no hotel with selected id
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCompany(@PathVariable Integer id) throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(id);
		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Hotel not found");
		}
		return new ResponseEntity<>(new HotelDTO(hotel.get()), HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<List<HotelDTO>> getCompany(@RequestParam(defaultValue = "") String hotelName,
			@RequestParam(defaultValue = "") String hotelAddress,
			@RequestParam(defaultValue = "") String hotelCheckInDate,
			@RequestParam(defaultValue = "") String hotelCheckOutDate) throws ParseException {

		Iterable<Hotel> hotels = hotelService.findSearchAll(hotelName, hotelAddress, hotelCheckInDate,
				hotelCheckOutDate);

		// convert hotels to DTO
		List<HotelDTO> ret = new ArrayList<>();
		for (Hotel hotel : hotels) {
			ret.add(new HotelDTO(hotel));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);

	}

	/**
	 * Adds new hotels.
	 * 
	 * @param hotel
	 * @return
	 */
	@PreAuthorize("hasAnyRole('SYS')")
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<HotelDTO> addHotel(@Valid @RequestBody HotelDTO hotelDTO) {
		Hotel hotel = new Hotel(hotelDTO.getName(), hotelDTO.getDescription());
		hotel.setLocation(hotelDTO.getLocation());
		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(hotel)), HttpStatus.CREATED);
	}

	/**
	 * Edit existing hotel.
	 * 
	 * @param hotel
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<HotelDTO> editHotel(@PathVariable Integer id, @Valid @RequestBody HotelDTO hotelDTO)
			throws ResourceNotFoundException {
		// hotel must exist
		Optional<Hotel> opt = hotelService.findHotel(hotelDTO.getId());

		// hotel is not found
		if (!opt.isPresent()) {
			throw new ResourceNotFoundException(hotelDTO.getId().toString(), "Hotel not found");
		}

		// set name and description
		opt.ifPresent(hotel -> {
			hotel.setName(hotelDTO.getName());
			hotel.setDescription(hotelDTO.getDescription());
			hotel.getLocation().setAddress(hotelDTO.getLocation().getAddress());
			hotel.getLocation().setCity(hotelDTO.getLocation().getCity());
			hotel.getLocation().setCountry(hotelDTO.getLocation().getCountry());
			hotel.getLocation().setLat(hotelDTO.getLocation().getLat());
			hotel.getLocation().setLon(hotelDTO.getLocation().getLon());
		});

		return new ResponseEntity<>(new HotelDTO(hotelService.saveHotel(opt.get())), HttpStatus.OK);
	}

	/**
	 * Adds new room to hotel.
	 * 
	 * @param car
	 * @return
	 */
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/addRoom/{hotelId}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RoomDTO> addRoom(@PathVariable Integer hotelId, @Valid @RequestBody RoomDTO roomDTO)
			throws ResourceNotFoundException {
		Room room = hotelService.addRoom(hotelId, roomDTO);
		return new ResponseEntity<>(new RoomDTO(room), HttpStatus.CREATED);
	}

	/**
	 * Edit room of hotel.
	 * 
	 * @param room
	 * @return
	 * @throws RequestDataException
	 */
	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/editRoom/{hotelId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<RoomDTO> editRoom(@PathVariable Integer hotelId, @Valid @RequestBody RoomDTO roomDTO)
			throws ResourceNotFoundException, RequestDataException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		for (Room r : hotel.get().getRooms()) {
			if (r.getId().equals(roomDTO.getId())) {
				if (notReserved(r)) {
					r.setRoomNumber(roomDTO.getRoomNumber());
					r.setFloor(roomDTO.getFloor());
					r.setNumberOfBeds(roomDTO.getNumberOfBeds());
					r.setPrice(roomDTO.getPrice());
					r.setType(roomDTO.getType());
					r.increaseVersion();
				} else
					throw new RequestDataException("Can't edit reserved room!");
			}
		}

		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(roomDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/deleteRoom/{hotelId}/{roomId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> deleteRoom(@PathVariable Integer hotelId, @PathVariable Integer roomId)
			throws ResourceNotFoundException, RequestDataException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Hotel h = hotel.get();

		for (Room r : h.getRooms()) {
			if (r.getId().equals(roomId)) {
				if (notReserved(r)) {
					r.setActive(false);
					r.increaseVersion();
					roomService.saveRoom(r);
					System.out.println("r");
					break;
				} else
					throw new RequestDataException("Can't delete reserved room!");
			}
		}
		System.out.println("Brisanje");
		System.out.println("hotel.get" + h.getId());

		return new ResponseEntity<>(roomId, HttpStatus.OK);
	}

	public Boolean notReserved(Room r) {
		Date today = new Date();
		for (SingleRoomReservation srr : r.getSingleRoomReservations()) {
			RoomReservation rr = srr.getRoomReservation();
			if (rr.getActive() && today.compareTo(rr.getCheckOutDate()) < 0) {
				return false;
			}
		}
		return true;
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/addAdditionalService/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> addBaggageInformation(@PathVariable Integer id,
			@Valid @RequestBody AdditionalService additionalService) throws ResourceNotFoundException {
		AdditionalService service = hotelService.addAdditionalService(additionalService, id);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/deleteAdditionalService/{hotelId}/{serviceId}", method = RequestMethod.DELETE, consumes = "application/json")
	public ResponseEntity<?> deleteService(@PathVariable Integer hotelId, @PathVariable Long serviceId)
			throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		for (AdditionalService as : hotel.get().getAdditionalServices()) {
			if (as.getId().equals(serviceId)) {
				as.setActive(false);
				break;
			}
		}

		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(serviceId, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/editAdditionalService/{hotelId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> editService(@PathVariable Integer hotelId,
			@Valid @RequestBody AdditionalService additionalService) throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		for (AdditionalService as : hotel.get().getAdditionalServices()) {
			if (as.getId().equals(additionalService.getId())) {
				as.setName(additionalService.getName());
				as.setDescription(additionalService.getDescription());
				as.setPrice(additionalService.getPrice());
				break;
			}
		}

		hotelService.saveHotel(hotel.get());
		return new ResponseEntity<>(additionalService, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/rentRoom/{hotelId}/{customer}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<RoomReservationDTO> rentRoom(@PathVariable Integer hotelId, @PathVariable String customer,
			@Valid @RequestBody RoomReservationDTO roomReservationDTO)
			throws ResourceNotFoundException, RequestDataException {
		RoomReservation roomReservation = roomService.addReservation(hotelId, customer, roomReservationDTO);
		return new ResponseEntity<>(new RoomReservationDTO(roomReservation), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/addServiceToFast/{hotelId}/{serviceId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> rentRoom(@PathVariable Integer hotelId, @PathVariable Long serviceId)
			throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Optional<AdditionalService> as = hotel.get().getAdditionalServices().stream()
				.filter(o -> o.getId().equals(serviceId)).findFirst();
		if (!as.isPresent()) {
			throw new ResourceNotFoundException(serviceId.toString(), "Service is not found in that hotel");
		}
 
		as.get().setIsFast(true);

		hotelService.saveService(as.get());

		System.out.println("Servis dodat u brzih!");
		return new ResponseEntity<>(as.get(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/removeServiceFromFast/{hotelId}/{serviceId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> removeServiceFromFast(@PathVariable Integer hotelId, @PathVariable Long serviceId)
			throws ResourceNotFoundException {
		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Optional<AdditionalService> as = hotel.get().getAdditionalServices().stream()
				.filter(o -> o.getId().equals(serviceId)).findFirst();
		if (!as.isPresent()) {
			throw new ResourceNotFoundException(serviceId.toString(), "Service is not found in that hotel");
		}

		// postaviti polje fast u servisu na false
		as.get().setIsFast(false);

		hotelService.saveService(as.get());

		System.out.println("Servis uklonjen iz brzih!");
		return new ResponseEntity<>(as.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getFastRooms/{hotelId}", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<RoomDTO>> getFastRooms(@PathVariable Integer hotelId) throws ResourceNotFoundException {

		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Iterable<Room> rooms = roomService.findAllFast(hotelId);

		// convert companies to DTO
		List<RoomDTO> ret = new ArrayList<>();
		for (Room r : rooms) {
			ret.add(new RoomDTO(r));
		}

		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@RequestMapping(value = "/getFastRooms/{city}/{date}/{ticketCount}", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<RoomDTO>> getFastRooms(@PathVariable String city, @PathVariable String date,
			@PathVariable Integer ticketCount) throws ResourceNotFoundException, ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf1.parse(date);

		Iterable<Hotel> hotels = hotelService.findAll();

		// convert companies to DTO
		List<RoomDTO> ret = new ArrayList<>();
		for (Hotel h : hotels) {

			if (h.getLocation().getCity().equals(city)) {
				for (Room r : h.getRooms()) {
					if (r.getIsFast() && r.getBeginDate().compareTo(start) == 0 && r.getNumberOfBeds() <= ticketCount) {
						ret.add(new RoomDTO(r));
					}
				}

			}

		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/makeRoomFast/{hotelId}/{roomId}/{discount}/{startDate}/{endDate}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> makeRoomFast(@PathVariable Integer hotelId, @PathVariable Integer roomId,
			@PathVariable Double discount, @PathVariable String startDate, @PathVariable String endDate)
			throws ResourceNotFoundException, ParseException {

		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Optional<Room> room = hotel.get().getRooms().stream().filter(o -> o.getId().equals(roomId)).findFirst();
		if (!room.isPresent()) {
			throw new ResourceNotFoundException(roomId.toString(), "Room not found in that hotel");
		}

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

		Date inDate = sdf1.parse(startDate);
		Date outDate = sdf1.parse(endDate);

		room.get().setIsFast(true);
		if (discount > room.get().getPrice()) {
			room.get().setDiscount(room.get().getPrice());
		} else {
			room.get().setDiscount(discount);
		}
		room.get().setBeginDate(inDate);
		room.get().setEndDate(outDate);

		Room r = roomService.saveRoom(room.get());
		System.out.println("Soba dodata u brze!");
		return new ResponseEntity<>(r, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('HOTELADMIN')")
	@AdminAccountActiveCheck
	@HotelAdminCheck
	@RequestMapping(value = "/makeRoomSlow/{hotelId}/{roomId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> makeRoomSlow(@PathVariable Integer hotelId, @PathVariable Integer roomId)
			throws ResourceNotFoundException, ParseException {

		Optional<Hotel> hotel = hotelService.findHotel(hotelId);

		if (!hotel.isPresent()) {
			throw new ResourceNotFoundException(hotelId.toString(), "Hotel not found");
		}

		Optional<Room> room = hotel.get().getRooms().stream().filter(o -> o.getId().equals(roomId)).findFirst();
		if (!room.isPresent()) {
			throw new ResourceNotFoundException(roomId.toString(), "Room not found in that hotel");
		}

		room.get().setIsFast(false);

		Room r = roomService.saveRoom(room.get());
		System.out.println("Soba uklonjena iz brzih!");
		return new ResponseEntity<>(r, HttpStatus.OK);
	}

}
