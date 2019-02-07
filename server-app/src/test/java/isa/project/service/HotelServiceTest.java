package isa.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import isa.project.constants.HotelConstants;
import isa.project.dto.hotel.RoomDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.hotel.Room;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.hotel.RoomRepository;
import isa.project.repository.shared.AdditionalServiceRepository;
import isa.project.service.hotel.HotelService;
import isa.project.service.hotel.RoomService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelServiceTest {

	@Mock
	private HotelRepository hotelRepository;

	@Mock
	private AdditionalServiceRepository additionalServiceRepository;

	@InjectMocks
	private HotelService hotelService;

	@Mock
	private Hotel hotel;

	@InjectMocks
	private RoomService roomService;

	@Mock
	private RoomRepository roomRepository;

	@Test
	public void testFindAll() {
		when(hotelRepository.findAll()).thenReturn(Arrays.asList(new Hotel("HotelName", "HotelDesc")));
		Iterable<Hotel> hotels = hotelService.findAll();
		assertThat(hotels).hasSize(1);

		verify(hotelRepository, times(1)).findAll();
		verifyNoMoreInteractions(hotelRepository);
	}

	@Test
	public void testAddAdditionalService() throws ResourceNotFoundException {
		Hotel h = new Hotel("HotelName", "HotelDesc");
		h.setAdditionalServices(new HashSet<>());
		when(hotelRepository.findById(2)).thenReturn(Optional.of(h));
		AdditionalService as = new AdditionalService("AsName", "AsDesc", 2.5);

		when(additionalServiceRepository.save(as)).thenReturn(as);
		// long dbSizeBeforeAdd =
		// hotelService.findAll().spliterator().getExactSizeIfKnown();

		AdditionalService dbAs = hotelService.addAdditionalService(as, 2);
		assertThat(dbAs).isNotNull();
		Assert.assertTrue(dbAs.getName().equals("AsName"));
		Assert.assertTrue(dbAs.getDescription().equals("AsDesc"));
		Assert.assertTrue(dbAs.getPrice().equals(new Double(2.5)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFindAllPageable() {
		PageRequest pageRequest = new PageRequest(1, 2);
		when(hotelRepository.findAll(pageRequest)).thenReturn(
				new PageImpl<Hotel>(Arrays.asList(new Hotel("Novi", "Opis")).subList(0, 1), pageRequest, 1));
		Page<Hotel> hotels = hotelService.findAll(pageRequest);
		assertThat(hotels).hasSize(1);
		verify(hotelRepository, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(hotelRepository);
	}

	@Test
	public void testFindHotel() {
		when(hotelRepository.findById(HotelConstants.DB_ID.intValue())).thenReturn(Optional.of(hotel));
		Optional<Hotel> opt = hotelService.findHotel(HotelConstants.DB_ID.intValue());

		assertEquals(Optional.of(hotel), opt);
		verify(hotelRepository, times(1)).findById(HotelConstants.DB_ID.intValue());
		verifyNoMoreInteractions(hotelRepository);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddRoom() throws ResourceNotFoundException {
		Hotel hotel = new Hotel(HotelConstants.NEW_HOTEL_NAME, HotelConstants.NEW_HOTEL_DESCRIPTION);
		hotel.setRooms(new HashSet<>());

		when(hotelRepository.findById(2)).thenReturn(Optional.of(hotel));

		when(roomRepository.findAll()).thenReturn(Arrays.asList(new Room(hotel, 4, 4, 4, 15.0, "Apartman")));

		Room room = new Room(hotel, 4, 4, 4, 15.0, "Apartman");
		when(roomRepository.save(room)).thenReturn(room);
		int dbSizeBeforeAdd = roomService.findAll().size();

		Room dbRoom = hotelService.addRoom(2, new RoomDTO(room));
		assertThat(dbRoom).isNotNull();

		when(roomRepository.findAll()).thenReturn(Arrays.asList(new Room(hotel, 4, 4, 4, 15.0, "Studio"), room));
		List<Room> rooms = roomService.findAll();

		assertThat(rooms).hasSize(dbSizeBeforeAdd + 1);
		dbRoom = rooms.get(rooms.size() - 1); // uzima poslednjeg, tj. dodatog

		Assert.assertTrue(dbRoom.getFloor() == 4);
		Assert.assertTrue(dbRoom.getRoomNumber() == 4);
		Assert.assertTrue(dbRoom.getNumberOfBeds() == 4);
		Assert.assertTrue(dbRoom.getPrice() == 15.0);
		Assert.assertTrue("Apartman".equals(dbRoom.getType()));

		verify(roomRepository, times(2)).findAll();
		verify(roomRepository, times(1)).save(room);
		verify(hotelRepository, times(1)).findById(2);
		verifyNoMoreInteractions(roomRepository);
		verifyNoMoreInteractions(hotelRepository);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testFindSearchAll() throws ResourceNotFoundException, ParseException {
		
		Hotel h = new Hotel(HotelConstants.HOTEL_NAME, HotelConstants.HOTEL_ADDRESS);
		h.setId(666); // Hotel Transilvanija
		h.setRooms(new HashSet<>());
		when(hotelRepository.findSearchAll(HotelConstants.HOTEL_NAME.toLowerCase(), HotelConstants.HOTEL_ADDRESS.toLowerCase())).thenReturn(Arrays.asList(h));
		
		Room room = new Room(h, 4, 4, 4, 15.0, "Apartman");
		h.getRooms().add(room);
		when(hotelRepository.findById(666)).thenReturn(Optional.of(h));
		
		Iterable<Hotel> list = hotelService.findSearchAll(HotelConstants.HOTEL_NAME, HotelConstants.HOTEL_ADDRESS, "", "");
		
		Assert.assertEquals(list.iterator().next().getName(), HotelConstants.HOTEL_NAME);
		assertThat(list).hasSize(1);
	}
	
}
