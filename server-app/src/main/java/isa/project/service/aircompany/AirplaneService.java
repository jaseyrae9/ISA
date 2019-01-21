package isa.project.service.aircompany;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isa.project.dto.aircompany.AirplaneDTO;
import isa.project.dto.aircompany.SeatDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.AirCompany;
import isa.project.model.aircompany.Airplane;
import isa.project.model.aircompany.Airplane.AirplaneStatus;
import isa.project.model.aircompany.Seat;
import isa.project.repository.aircompany.AirCompanyRepository;
import isa.project.repository.aircompany.AirplaneRepository;

@Service
public class AirplaneService {

	@Autowired
	private AirplaneRepository airplaneRepository;

	@Autowired
	private AirCompanyRepository airCompanyRepository;

	/**
	 * Prosleđuje sve avione sa statusima ACTIVE i IN_PROGRESS neke aviokompanije.
	 * 
	 * @param id - oznaka aviokompanije
	 * @return - lista aviona
	 * @throws ResourceNotFoundException - ako aviokompanija nije pronađena
	 */
	public ArrayList<AirplaneDTO> getAllAirplanes(Integer id) throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje novi avion
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();
		// prosledi sve avione koji nisu obrisani
		ArrayList<AirplaneDTO> airplanes = new ArrayList<>();
		for (Airplane airplane : airCompany.getAirplanes()) {
			if (airplane.getStatus() != AirplaneStatus.DELETED) {
				airplanes.add(new AirplaneDTO(airplane));
			}
		}
		return airplanes;
	}
	
	/**
	 * Prosleđuje sve avione sa statusima ACTIVE neke aviokompanije.
	 * 
	 * @param id - oznaka aviokompanije
	 * @return - lista aviona
	 * @throws ResourceNotFoundException - ako aviokompanija nije pronađena
	 */
	public ArrayList<AirplaneDTO> getActiveAirplanes(Integer id) throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje novi avion
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(id);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();
		// prosledi sve avione koji nisu obrisani
		ArrayList<AirplaneDTO> airplanes = new ArrayList<>();
		for (Airplane airplane : airCompany.getAirplanes()) {
			if (airplane.getStatus() == AirplaneStatus.ACTIVE) {
				airplanes.add(new AirplaneDTO(airplane));
			}
		}
		return airplanes;
	}

	/**
	 * Kreira novi avion koji pripada nekoj aviokompaniji.
	 * 
	 * @param airCompanyId - oznaka aviokompanije kojoj pripada avion.
	 * @param airplaneDTO  - podaci o avionu.
	 * @return - DTO objekat novo kreiranog aviona.
	 * @throws ResourceNotFoundException - ukoliko avio kompanija nije pronađena.
	 */
	public AirplaneDTO addNewAirplane(Integer airCompanyId, AirplaneDTO airplaneDTO) throws ResourceNotFoundException {
		// pronadji avio kompaniju u koju se dodaje novi avion
		Optional<AirCompany> airCompanyOpt = airCompanyRepository.findById(airCompanyId);
		if (!airCompanyOpt.isPresent()) {
			throw new ResourceNotFoundException(airCompanyId.toString(), "Air company not found.");
		}
		AirCompany airCompany = airCompanyOpt.get();

		// kreiraj novi avion
		Airplane airplane = new Airplane(airplaneDTO);
		airplane.setStatus(AirplaneStatus.IN_PROGRESS);
		airplane.setAirCompany(airCompany);

		return new AirplaneDTO(airplaneRepository.save(airplane));
	}

	/**
	 * Uređuje postojeći avion.
	 * 
	 * @param airplaneDTO - informacije o avionu.
	 * @return - DTO objekat uređenog aviona.
	 * @throws ResourceNotFoundException - ako avion nije pronađen.
	 * @throws RequestDataException      - ako avion nije u statusu IN_PROGRESS
	 */
	public AirplaneDTO editAirplane(AirplaneDTO airplaneDTO) throws ResourceNotFoundException, RequestDataException {
		// pronadji avion
		Optional<Airplane> airplaneOpt = airplaneRepository.findById(airplaneDTO.getId());
		if (!airplaneOpt.isPresent()) {
			throw new ResourceNotFoundException(airplaneDTO.getId().toString(), "Airplane not found.");
		}
		Airplane airplane = airplaneOpt.get();

		if (airplane.getStatus() != AirplaneStatus.IN_PROGRESS) {
			throw new RequestDataException(
					"Airplane can not be edited. Airplane status must be IN PROGRESS for editing to be allowed.");
		}

		airplane.setName(airplaneDTO.getName());
		airplane.setColNum(airplaneDTO.getColNum());
		airplane.setRowNum(airplane.getRowNum());
		airplane.setSeatsPerCol(airplane.getSeatsPerCol());
		for (int i = 0; i < airplaneDTO.getSeats().size(); ++i) {
			SeatDTO seatDTO = airplaneDTO.getSeats().get(i);
			if (i < airplane.getSeats().size()) {
				Seat seat = airplane.getSeats().get(i);
				seat.setSeatClass(seatDTO.getSeatClass());
				seat.setColNum(seatDTO.getColNum());
				seat.setRowNum(seatDTO.getRowNum());
			} else {
				airplane.getSeats().add(new Seat(seatDTO));
			}
		}

		return new AirplaneDTO(airplaneRepository.save(airplane));
	}

	/**
	 * Postavlja status aviona na obrisan. Avion čiji status je obrisan ne može da
	 * se koristi za letove.
	 * 
	 * @param id - oznaka aviona
	 * @throws ResourceNotFoundException - ako avion nije pronađen
	 * @throws RequestDataException      - ako prosleđeni avion ima status obrisan
	 */
	public void deleteAirplane(Integer id) throws ResourceNotFoundException, RequestDataException {
		// pronadji avion
		Optional<Airplane> airplaneOpt = airplaneRepository.findById(id);
		if (!airplaneOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Airplane not found.");
		}
		Airplane airplane = airplaneOpt.get();

		if (airplane.getStatus() == AirplaneStatus.DELETED) {
			throw new RequestDataException("Airplane already deleted.");
		}

		airplane.setStatus(AirplaneStatus.DELETED);
		airplaneRepository.save(airplane);
	}
	
	/**
	 * Postavlja status aviona na aktivan. Avion čiji status je aktivan može biti uporebljen za letove.
	 * 
	 * @param id - oznaka aviona
	 * @return - izmenjeni avion
	 * @throws ResourceNotFoundException - ako avion nije pronađen
	 * @throws RequestDataException      - ako prosleđeni avion ima status obrisan
	 */
	public AirplaneDTO activateAirplane(Integer id) throws ResourceNotFoundException, RequestDataException {
		// pronadji avion
		Optional<Airplane> airplaneOpt = airplaneRepository.findById(id);
		if (!airplaneOpt.isPresent()) {
			throw new ResourceNotFoundException(id.toString(), "Airplane not found.");
		}
		Airplane airplane = airplaneOpt.get();

		if (airplane.getStatus() != AirplaneStatus.IN_PROGRESS) {
			throw new RequestDataException("Airplane can not be activated. Only airplanes in status IN_PROGRESS can be activated.");
		}

		airplane.setStatus(AirplaneStatus.ACTIVE);
		return new AirplaneDTO(airplaneRepository.save(airplane));
	}
}
