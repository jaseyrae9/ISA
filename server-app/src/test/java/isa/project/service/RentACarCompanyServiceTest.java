package isa.project.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import isa.project.constants.RentACarCompanyConstants;
import isa.project.dto.rentacar.BranchOfficeDTO;
import isa.project.dto.rentacar.CarDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.rentacar.BranchOffice;
import isa.project.model.rentacar.Car;
import isa.project.model.rentacar.CarReservation;
import isa.project.model.rentacar.RentACarCompany;
import isa.project.repository.rentacar.BranchOfficeRepository;
import isa.project.repository.rentacar.CarRepository;
import isa.project.repository.rentacar.RentACarCompanyRepository;
import isa.project.service.rentacar.CarService;
import isa.project.service.rentacar.RentACarCompanyService;
import static isa.project.constants.RentACarCompanyConstants.RENT_A_CAR_COMPANY_ID;
import static isa.project.constants.RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_NAME;
import static isa.project.constants.RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_DESCRIPTION;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentACarCompanyServiceTest {

	@Mock
	private RentACarCompanyRepository rentACarRepository;

	@Mock
	private BranchOfficeRepository branchOfficeRepository;

	@Mock
	private CarRepository carRepository;

	@InjectMocks
	private RentACarCompanyService rentACarCompanyService;

	@InjectMocks
	private CarService carService;

	@Mock
	private RentACarCompany company;

	@Mock
	private Car car;

	@Mock
	private CarReservation carReservation;

	@Test
	public void testFindAll() {
		List<RentACarCompany> companies = new ArrayList<>();
		RentACarCompany company = new RentACarCompany();
		company.setName(RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_NAME);
		company.setDescription(RentACarCompanyConstants.NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		companies.add(company);

		when(rentACarRepository.findAll()).thenReturn(companies);
		Iterable<RentACarCompany> c = rentACarCompanyService.findAll();
		assertThat(c).hasSize(1);

		verify(rentACarRepository, times(1)).findAll();
		verifyNoMoreInteractions(rentACarRepository);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFindAllPageable() {
		PageRequest pageRequest = new PageRequest(1, 2);
		when(rentACarRepository.findAll(pageRequest)).thenReturn(new PageImpl<RentACarCompany>(
				Arrays.asList(new RentACarCompany(NEW_RENT_A_CAR_COMPANY_NAME, NEW_RENT_A_CAR_COMPANY_DESCRIPTION))
						.subList(0, 1),
				pageRequest, 1));
		Page<RentACarCompany> companies = rentACarCompanyService.findAll(pageRequest);
		assertThat(companies).hasSize(1);
		verify(rentACarRepository, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(rentACarRepository);
	}

	@Test
	public void testFindRentACarCompany() {
		when(rentACarRepository.findById(RENT_A_CAR_COMPANY_ID.intValue())).thenReturn(Optional.of(company));
		Optional<RentACarCompany> opt = rentACarCompanyService.findRentACarCompany(RENT_A_CAR_COMPANY_ID.intValue());

		assertEquals(Optional.of(company), opt);
		verify(rentACarRepository, times(1)).findById(RENT_A_CAR_COMPANY_ID.intValue());
		verifyNoMoreInteractions(rentACarRepository);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddCar() throws ResourceNotFoundException {
		RentACarCompany rentacar = new RentACarCompany(NEW_RENT_A_CAR_COMPANY_NAME, NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		rentacar.setCars(new HashSet<>());
		when(rentACarRepository.findById(2)).thenReturn(Optional.of(rentacar));

		when(carRepository.findAll())
				.thenReturn(Arrays.asList(new Car(rentacar, "brand", "model", 2015, 4, 5, 15.0, "Sedan")));

		Car car = new Car(rentacar, "brand", "model", 2015, 4, 5, 15.0, "Sedan");
		when(carRepository.save(car)).thenReturn(car);
		int dbSizeBeforeAdd = carService.findAll().size();
		System.out.println("size" + dbSizeBeforeAdd);
		Car dbCar = rentACarCompanyService.addCar(2, new CarDTO(car));
		assertThat(dbCar).isNotNull();

		when(carRepository.findAll())
				.thenReturn(Arrays.asList(new Car(rentacar, "aaa", "aa", 2015, 4, 5, 15.0, "Sedan"), car));
		List<Car> cars = carService.findAll();

		assertThat(cars).hasSize(dbSizeBeforeAdd + 1);
		dbCar = cars.get(cars.size() - 1); // uzima poslednjeg, tj. dodatog
		Assert.assertTrue("brand".equals(dbCar.getBrand()));
		Assert.assertTrue("model".equals(dbCar.getModel()));
		Assert.assertTrue(dbCar.getYearOfProduction() == 2015);
		Assert.assertTrue(dbCar.getSeatsNumber() == 4);
		Assert.assertTrue(dbCar.getDoorsNumber() == 5);
		Assert.assertTrue(dbCar.getPrice() == 15.0);
		Assert.assertTrue("Sedan".equals(dbCar.getType()));

		verify(carRepository, times(2)).findAll();
		verify(carRepository, times(1)).save(car);
		verify(rentACarRepository, times(1)).findById(2);
		verifyNoMoreInteractions(carRepository);
		verifyNoMoreInteractions(rentACarRepository);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testSearchAll() throws ParseException {
		RentACarCompany company = new RentACarCompany(NEW_RENT_A_CAR_COMPANY_NAME, NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		company.setId(2);
		company.setCars(new HashSet<>());

		when(rentACarRepository.searchNameAndAddress(NEW_RENT_A_CAR_COMPANY_NAME.toLowerCase(),
				NEW_RENT_A_CAR_COMPANY_DESCRIPTION.toLowerCase())).thenReturn(Arrays.asList(company));

		Car car = new Car(company, "brand", "model", 2015, 4, 5, 15.0, "Sedan");
		company.getCars().add(car);
		when(rentACarRepository.findById(2)).thenReturn(Optional.of(company));

		Iterable<RentACarCompany> list = rentACarCompanyService.searchAll(NEW_RENT_A_CAR_COMPANY_NAME,
				NEW_RENT_A_CAR_COMPANY_DESCRIPTION, "", "");

		Assert.assertEquals(list.iterator().next().getName(), NEW_RENT_A_CAR_COMPANY_NAME);
		assertThat(list).hasSize(1);
		verify(rentACarRepository, times(1)).searchNameAndAddress(NEW_RENT_A_CAR_COMPANY_NAME.toLowerCase(),
				NEW_RENT_A_CAR_COMPANY_DESCRIPTION.toLowerCase());
		verify(rentACarRepository, times(1)).findById(2);
		verifyNoMoreInteractions(rentACarRepository);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testGetRentACarMonthlyInfo() {
		when(car.getBrand()).thenReturn("Fiat");
		Set<Car> cars = new HashSet<>();
		cars.add(car);
		when(company.getCars()).thenReturn(cars);
		when(carReservation.getActive()).thenReturn(true);
		Date today = new Date();
		when(carReservation.getPickUpDate()).thenReturn(today);
		when(carReservation.getDropOffDate()).thenReturn(today);
		Set<CarReservation> crSet = new HashSet<>();
		crSet.add(carReservation);
		when(car.getCarReservations()).thenReturn(crSet);
		MonthlyReportDTO mrDto = rentACarCompanyService.getRentACarMonthlyInfo(company);
		assertThat(mrDto.getMonthly()).hasSize(12);
		assertEquals(mrDto.getMonthly().get(1).intValue(), 1);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testGetRentACarCompanyIncome() throws ParseException {
		when(car.getBrand()).thenReturn("Fiat");
		when(car.getPrice()).thenReturn(2.2);
		Set<Car> cars = new HashSet<>();
		cars.add(car);
		when(company.getCars()).thenReturn(cars);
		Date today = new Date();
		when(carReservation.getActive()).thenReturn(true);
		when(carReservation.getPickUpDate()).thenReturn(today);
		when(carReservation.getDropOffDate()).thenReturn(today);
		Set<CarReservation> crSet = new HashSet<>();
		crSet.add(carReservation);
		when(car.getCarReservations()).thenReturn(crSet);
		Double price = rentACarCompanyService.getIncome(company, "2019-01-01", "2019-12-01");
		assertEquals(price, new Double(2.2));
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddBranchOffice() throws ResourceNotFoundException {
		RentACarCompany rentacar = new RentACarCompany(NEW_RENT_A_CAR_COMPANY_NAME, NEW_RENT_A_CAR_COMPANY_DESCRIPTION);
		rentacar.setBranchOffices(new HashSet<>());
		when(rentACarRepository.findById(2)).thenReturn(Optional.of(rentacar));

		BranchOffice bo = new BranchOffice(rentacar, "ime");
		when(branchOfficeRepository.save(bo)).thenReturn(bo);
		
		BranchOffice dbBo = rentACarCompanyService.addBranchOffice(2, new BranchOfficeDTO(bo));
		assertThat(dbBo).isNotNull();
		assertTrue("ime".equals(dbBo.getName()));
		assertTrue(dbBo.getActive());
	
		verify(branchOfficeRepository, times(1)).save(bo);
		verify(rentACarRepository, times(1)).findById(2);
		verifyNoMoreInteractions(branchOfficeRepository);
		verifyNoMoreInteractions(rentACarRepository);
	}
	
}
