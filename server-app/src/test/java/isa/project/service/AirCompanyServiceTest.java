package isa.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import isa.project.constants.AirCompanyConstants;
import isa.project.model.aircompany.AirCompany;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.service.aircompany.AirCompanyService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AirCompanyServiceTest {

	@Mock
	private AirCompanyRepository airCompanyRepository;
	
	@Mock
	private AirCompany company;
	
	@InjectMocks
	private AirCompanyService airCompanyService;
	
	@Test
	public void testFindAll() {
		List<AirCompany> companies = new ArrayList<>();
		AirCompany company = new AirCompany();
		company.setName(AirCompanyConstants.NEW_AIR_COMPANY_NAME);
		company.setDescription(AirCompanyConstants.NEW_AIR_DESCRIPTION);
		companies.add(company);

		when(airCompanyRepository.findAll()).thenReturn(companies);
		Iterable<AirCompany> c = airCompanyService.findAll();
		assertThat(c).hasSize(1);

		verify(airCompanyRepository, times(1)).findAll();
		verifyNoMoreInteractions(airCompanyRepository);
	}

	@Test
	public void testFindAirCompany() {
		when(airCompanyRepository.findById(AirCompanyConstants.AIR_COMPANY_ID.intValue()))
				.thenReturn(Optional.of(company));
		Optional<AirCompany> opt = airCompanyService
				.findAircompany(AirCompanyConstants.AIR_COMPANY_ID.intValue());

		assertEquals(Optional.of(company), opt);
		verify(airCompanyRepository, times(1)).findById(AirCompanyConstants.AIR_COMPANY_ID.intValue());
		verifyNoMoreInteractions(airCompanyRepository);
	}
}
