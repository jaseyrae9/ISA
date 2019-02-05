package isa.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.hotel.Hotel;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.hotel.HotelRepository;
import isa.project.repository.shared.AdditionalServiceRepository;
import isa.project.service.hotel.HotelService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelServiceTest {

	@Mock
	private HotelRepository hotelRepository;
	
	@Mock
	private AdditionalServiceRepository additionalServiceRepository;
	
	@InjectMocks
	private HotelService hotelService;
	
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
		// long dbSizeBeforeAdd = hotelService.findAll().spliterator().getExactSizeIfKnown();
	
		AdditionalService dbAs = hotelService.addAdditionalService(as, 2);
		assertThat(dbAs).isNotNull();
		Assert.assertTrue(dbAs.getName().equals("AsName"));
		Assert.assertTrue(dbAs.getDescription().equals("AsDesc"));
		Assert.assertTrue(dbAs.getPrice().equals(new Double(2.5)));
	}
	
	/*@Test
	public void testFindAllPageable() {
		
		PageRequest pageRequest = new PageRequest(1, 2); //second page
		when(studentRepositoryMock.findAll(pageRequest)).thenReturn(new PageImpl<Student>(Arrays.asList(new Student(DB_ID, NEW_INDEX, NEW_FIRST_NAME, NEW_LAST_NAME)).subList(0, 1), pageRequest, 1));
		Page<Student> students = studentService.findAll(pageRequest);
		assertThat(students).hasSize(1);
		verify(studentRepositoryMock, times(1)).findAll(pageRequest);
        verifyNoMoreInteractions(studentRepositoryMock);
	}*/
	

}
