package isa.project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import isa.project.constants.AirCompanyConstants;
import isa.project.dto.aircompany.DestinationDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.DestinationRepository;
import isa.project.service.aircompany.AirCompanyService;
import static isa.project.constants.AirCompanyConstants.NEW_AIR_DESCRIPTION;
import static isa.project.constants.AirCompanyConstants.NEW_AIR_COMPANY_NAME;
import static isa.project.constants.AirCompanyConstants.AIR_COMPANY_ID;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AirCompanyServiceTest {

	@Mock
	private AirCompanyRepository airCompanyRepository;
	
	@Mock
	private AirCompany company;
	
	@Mock
	private DestinationRepository destinationRepository;
	
	@InjectMocks
	private AirCompanyService airCompanyService;
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	
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
	
	@SuppressWarnings("deprecation")
	@Test
	public void testFindAllPageable() {		
		PageRequest pageRequest = new PageRequest(1, 2); 
		when(airCompanyRepository.findAll(pageRequest)).thenReturn(new PageImpl<AirCompany>(Arrays.asList(new AirCompany(NEW_AIR_COMPANY_NAME, NEW_AIR_DESCRIPTION)).subList(0, 1), pageRequest, 1));
		Page<AirCompany> companies = airCompanyService.findAll(pageRequest);
		assertThat(companies).hasSize(1);
		verify(airCompanyRepository, times(1)).findAll(pageRequest);
        verifyNoMoreInteractions(airCompanyRepository);
	}
	

	@Test
    @Transactional
    @Rollback(true)
	public void testAddDestination() throws ResourceNotFoundException {
		AirCompany company= new AirCompany(NEW_AIR_COMPANY_NAME, NEW_AIR_DESCRIPTION);
		company.setDestinations(new HashSet<>());
		when(airCompanyRepository.findById(2)).thenReturn(Optional.of(company));
	
		Destination destination = new Destination(company, "RUM", "Srbija", "Ruma", "Milosevo dvoriste");
		when(destinationRepository.save(destination)).thenReturn(destination);   
		
		Destination dbDestination = airCompanyService.addDestination(new DestinationDTO(destination), 2);
		
		assertThat(dbDestination).isNotNull();
		Assert.assertTrue("RUM".equals(dbDestination.getLabel()));
		Assert.assertTrue("Ruma".equals(dbDestination.getCity()));
		Assert.assertTrue("Srbija".equals(dbDestination.getCountry()));
		Assert.assertTrue("Milosevo dvoriste".equals(dbDestination.getAirportName()));
	
		verify(destinationRepository, times(1)).save(destination);
		verify(airCompanyRepository, times(1)).findById(2);
        verifyNoMoreInteractions(airCompanyRepository);
        verifyNoMoreInteractions(destinationRepository);
	}
	
	@Test
    @Transactional
    @Rollback(true)
	public void testDeleteDestination() throws ResourceNotFoundException, RequestDataException {
		exceptionRule.expect(ResourceNotFoundException.class);
	    exceptionRule.expectMessage("Air company not found.");
		airCompanyService.deleteDestination(AirCompanyConstants.DESTINATION_ID,AIR_COMPANY_ID.intValue());
		airCompanyService.deleteDestination(AirCompanyConstants.DESTINATION_ID,AIR_COMPANY_ID.intValue());
		
	}
	

	@Test
    @Transactional
    @Rollback(true)
	public void testDeleteDestinationExp() throws ResourceNotFoundException, RequestDataException {
		exceptionRule.expect(ResourceNotFoundException.class);
	    exceptionRule.expectMessage("Air company not found.");
		airCompanyService.deleteDestination(new Long(9898),new Integer(8989));
	}
	
}
