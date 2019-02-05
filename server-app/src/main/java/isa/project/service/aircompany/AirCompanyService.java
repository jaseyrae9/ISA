package isa.project.service.aircompany;

import static java.lang.Math.toIntExact;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isa.project.common.DateProvider;
import isa.project.dto.aircompany.DestinationDTO;
import isa.project.dto.shared.DailyReportDTO;
import isa.project.dto.shared.MonthlyReportDTO;
import isa.project.dto.shared.WeeklyReportDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Destination;
import isa.project.model.shared.AdditionalService;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.DestinationRepository;
import isa.project.repository.shared.AdditionalServiceRepository;

@Service
public class AirCompanyService {
	@Autowired
	private AirCompanyRepository airCompanyRepository;
	@Autowired
	private DestinationRepository destinationRepository;
	@Autowired
	private AdditionalServiceRepository additionalServiceRepository;
	@Autowired
	private DateProvider dateProvider;

	public Iterable<AirCompany> findAll() {
		return airCompanyRepository.findAll();
	}

	public Page<AirCompany> findAll(Pageable page) {
		return airCompanyRepository.findAll(page);
	}

	public Optional<AirCompany> findAircompany(Integer id) {
		return airCompanyRepository.findById(id);
	}

	public AirCompany saveAirCompany(AirCompany company) {
		return airCompanyRepository.save(company);
	}

	/**
	 * Kreira godisnji izvestaj za aviompaniju.
	 * @param id - oznaka aviokompanije
	 * @return - godisnji izvestaj po mesecima
	 */
	public MonthlyReportDTO getSoldTicketsPerMonth(Integer id){	
		MonthlyReportDTO report = new MonthlyReportDTO();
		for(Object[] result: airCompanyRepository.getSoldTicketsPerMonth(id, dateProvider.getCurrentYear())) {
			report.increaseMonthly((int)result[1] - 1, toIntExact((long)result[0]));
		}
		return report;
	}
	
	/**
	 * Generise mesecni izvestaj.
	 * @param id
	 * @return - mesecni izvestaj po sedmicama.
	 */
	public WeeklyReportDTO getSoldTicketsPerWeek(Integer id) {
		WeeklyReportDTO report = new WeeklyReportDTO();
		Date start = Date.from(dateProvider.getStartOfMonth().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(dateProvider.getEndOfMonth().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		for(Object result: airCompanyRepository.getSoldTicketsInPeriod(id, start, end)) {
			int week = dateProvider.getWeekOfMonth((Date)result);
			report.increaseWeekly(week-1, 1);
		}
		return report;
	}
	
	/**
	 * Kreira sedmicni izvestaj.
	 * @param id
	 * @return - sedmicni izvestaj po danima
	 */
	public DailyReportDTO getSoldTicketsPerDay(Integer id) {
		DailyReportDTO report = new DailyReportDTO();
		Integer weekStart = dateProvider.getStartOfWeek().getDayOfMonth();
		Date start = Date.from(dateProvider.getStartOfWeek().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(dateProvider.getEndOfWeek().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		for(Object[] result: airCompanyRepository.getSoldTicketsPerDay(id, start, end)){
			report.increaseDaily((int)result[1] - weekStart, toIntExact((long)result[0]));
		}
		return report;
	}
	
	public Double getProfitInPeriod(Integer id, Date from, Date to) {
		return airCompanyRepository.getProfitInPeriod(id, from, to);
	}

	/**
	 * Dodaje novu destinaciju na koju avio kompanija putuje.
	 * 
	 * @param destinationDTO - informacije o destinaciji
	 * @param id             - id aviokompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe avio kompanija
	 */
	public Destination addDestination(DestinationDTO destinationDTO, Integer id) throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje destinacija
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// kreiraj destinaciju
		Destination destination = new Destination(airCompany, destinationDTO.getLabel(), destinationDTO.getCountry(),
				destinationDTO.getCity(), destinationDTO.getAirportName());

		// sacuvaj
		return destinationRepository.save(destination);
	}

	/**
	 * Deaktivira destinaciju.
	 * 
	 * @param destinationId - oznaka destinacije
	 * @param airport       - oznaka aerodruma
	 * @throws ResourceNotFoundException - ako nisu pronađeni aerodrum ili
	 *                                   destinacija
	 * @throws RequestDataException      - ako je destinacija već izbrisana
	 */
	public void deleteDestination(Long destinationId, Integer airport)
			throws ResourceNotFoundException, RequestDataException {
		Destination destination = findDestination(destinationId, airport);
		if (!destination.getActive())
			throw new RequestDataException("Destination already deleted.");
		destination.setActive(false);
		destinationRepository.save(destination);
	}

	/**
	 * Za uređivanje postojeće destinacije. Deaktivira staru destinaciju i kreira
	 * novu.
	 * 
	 * @param destinationId  - oznaka destiancije
	 * @param airport        - oznaka aerodruma
	 * @param destinationDTO - informacije o destianciji
	 * @throws ResourceNotFoundException - ako nisu pronađeni aerodrum ili
	 *                                   destinacija
	 * @throws RequestDataException      - ako je destinacija izbrisana
	 */
	public Destination editDestination(Long destinationId, Integer airport, DestinationDTO destinationDTO)
			throws ResourceNotFoundException, RequestDataException {
		// obrisi staru
		deleteDestination(destinationId, airport);
		// kreiraj novu
		return addDestination(destinationDTO, airport);
	}

	/**
	 * Pronalazi destinaciju sa prosleđenim id.
	 * 
	 * @param destinationId - oznaka destinacije
	 * @param airport       - oznaka aerodruma
	 * @return - destinacija
	 * @throws ResourceNotFoundException - ako nisu pronađeni aerodrum ili
	 *                                   destinacija
	 */
	private Destination findDestination(Long destinationId, Integer airport) throws ResourceNotFoundException {
		// pronadji avio kompaniju u kojoj pripada destinacija
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(airport);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(airport.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();
		Optional<Destination> destination = airCompany.getDestinations().stream()
				.filter(d -> d.getId().equals(destinationId)).findFirst();
		if (!destination.isPresent()) {
			throw new ResourceNotFoundException(destinationId.toString(), "Destination not found.");
		}
		return destination.get();
	}

	/**
	 * Dodaje novu informaciju o prtljagu u aviokompaniju.
	 * 
	 * @param baggegeInformation - informacije o prtljagu
	 * @param id                 - id aviokompanije kojoj se dodaje destinacija
	 * @return
	 * @throws ResourceNotFoundException - ako se ne nađe avio kompanija
	 */
	public AdditionalService addBaggageInformation(AdditionalService baggageInformation, Integer id)
			throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje destinacija
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// sacuvaj dodatnu uslugu
		baggageInformation.setActive(true);
		AdditionalService service = additionalServiceRepository.save(baggageInformation);

		// sacuvaj informaciju o prtljagu u aviokompaniji
		airCompany.addBaggageInformation(baggageInformation);
		airCompanyRepository.save(airCompany);

		return service;
	}

	/**
	 * Logičko brisanje informacije o prtljagu.
	 * 
	 * @param baggageId - oznaka prtljaga
	 * @param airport   - oznaka aviokompanije
	 * @throws ResourceNotFoundException - ako aviokompanija ili informacija o
	 *                                   prtljagu nisu pronađeni
	 * @throws RequestDataException      - ako se pokušava brisati već obrisana
	 *                                   informacija
	 */
	public void deleteBaggageInformation(Long baggageId, Integer aircomapny)
			throws ResourceNotFoundException, RequestDataException {
		AdditionalService baggageInformation = findBaggageInformation(baggageId, aircomapny);
		if (!baggageInformation.getActive())
			throw new RequestDataException("Baggage Information aleardy deleted.");
		baggageInformation.setActive(false);
		additionalServiceRepository.save(baggageInformation);
	}

	/**
	 * Uređuje postojeću informaciju o prtljagu.
	 * 
	 * @param baggageInfo - podaci za informaciju o prtljagu
	 * @param aircomapny  - aviokompanija
	 * @return - uređena informacija
	 * @throws ResourceNotFoundException - ako nisu pronađeni aviokompanija ili
	 *                                   informacija o prtljagu.
	 * @throws RequestDataException      - ako je informacija obrisana.
	 */
	public AdditionalService editBaggageInformation(AdditionalService baggageInfo, Integer aircomapny)
			throws ResourceNotFoundException, RequestDataException {
		AdditionalService baggageInformation = findBaggageInformation(baggageInfo.getId(), aircomapny);
		if (!baggageInformation.getActive())
			throw new RequestDataException("Baggage Information is deleted.");
		baggageInformation.setName(baggageInfo.getName());
		baggageInformation.setDescription(baggageInfo.getDescription());
		baggageInformation.setPrice(baggageInfo.getPrice());
		return additionalServiceRepository.save(baggageInformation);
	}

	/**
	 * Pronalazi informaciju o prtljagu vezanu za aviokompaniju.
	 * 
	 * @param baggageId - oznaka informacije
	 * @param airport   - oznaka aviokompanije
	 * @return - informaciju o prtljagu
	 * @throws ResourceNotFoundException - ako se ne pronađu aviokompanija ili
	 *                                   informacija o prtljagu
	 */
	private AdditionalService findBaggageInformation(Long baggageId, Integer aircompany)
			throws ResourceNotFoundException {
		// pronadji avio kompaniju u kojoj pripada prtljag
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(aircompany);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(aircompany.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();
		Optional<AdditionalService> baggageInfo = airCompany.getBaggageInformation().stream()
				.filter(d -> d.getId().equals(baggageId)).findFirst();
		if (!baggageInfo.isPresent()) {
			throw new ResourceNotFoundException(baggageId.toString(), "Baggage information not found.");
		}
		return baggageInfo.get();
	}
}
