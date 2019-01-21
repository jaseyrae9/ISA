package isa.project.controller.aircompany;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.aircompany.AirplaneDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.service.aircompany.AirplaneService;

@RestController
@RequestMapping(value = "/aircompanies")
public class AirplaneController {

	@Autowired
	private AirplaneService airplaneService;

	/**
	 * Kreira novi avion za avio kompaniju.
	 * 
	 * @param id          - oznaka avio kompanije kojoj avion pripada.
	 * @param airplaneDTO - podaci o avionu.
	 * @return - DTO objekat novokreiranog aviona.
	 * @throws ResourceNotFoundException - ukoliko aviokomapnija nije pronađena.
	 */
	@RequestMapping(value = "/addAirplane/{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<?> createAirplane(@PathVariable Integer id, @Valid @RequestBody AirplaneDTO airplaneDTO)
			throws ResourceNotFoundException {
		return new ResponseEntity<>(airplaneService.addNewAirplane(id, airplaneDTO), HttpStatus.CREATED);
	}

	/**
	 * Uređuje postojeći avion.
	 * 
	 * @param id - id aviokompanije kojoj avion pripada.
	 * @param airplaneDTO - informacije o avionu.
	 * @return - DTO objekat aviona.
	 * @throws ResourceNotFoundException - ako avion nije pronađen.
	 * @throws RequestDataException - ako status aviona nije IN_PROGRESS, pa uređivanje nije moguće
	 */
	@RequestMapping(value = "/editAirplane/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<?> editAirplane(@PathVariable Integer id, @Valid @RequestBody AirplaneDTO airplaneDTO)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(airplaneService.editAirplane(airplaneDTO), HttpStatus.OK);
	}
	
	/**
	 * Menja status aviona u aktivan. 
	 * @param id - oznaka aviokompanije kojoj avion pripada.
	 * @param airplane - oznaka aviona.
	 * @return - izmenjeni avion.
	 * @throws ResourceNotFoundException - ako avion nije pronađen.
	 * @throws RequestDataException - ako avion nije u statusu IN_PROGRESS
	 */
	@RequestMapping(value = "/activateAirplane/{id}/{airplane}", method = RequestMethod.PUT)
	public ResponseEntity<?> activateAirplane(@PathVariable Integer id, @PathVariable Integer airplane) throws ResourceNotFoundException, RequestDataException{
		return new ResponseEntity<>(airplaneService.activateAirplane(airplane), HttpStatus.OK);
	}
	
	/**
	 * Menja status aviona u obrisan.
	 * 
	 * @param id - oznaka aviokompanije kojoj avion pripada.
	 * @param airplane - oznaka aviona.
	 * @return
	 * @throws ResourceNotFoundException - ukoliko avion sa datom oznakom nije pornađen.
	 * @throws RequestDataException - ako prosleđeni avion ima status obrisan
	 */
	@RequestMapping(value = "/deleteAirplane/{id}/{airplane}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAirplane(@PathVariable Integer id, @PathVariable Integer airplane) throws ResourceNotFoundException, RequestDataException{
		airplaneService.deleteAirplane(airplane);
		return ResponseEntity.ok().build();
	}

	/**
	 * Vraća sve avione aviokompanije čiji status je ACTIVE ili IN_PROGRESS.
	 * @param id - oznaka aviokomapnije.
	 * @return - lista aviona.
	 * @throws ResourceNotFoundException - ako aviokompanija nije pronađena.
	 */
	@RequestMapping(value = "/getAirplanes/{id}", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<AirplaneDTO>> getAllAirplanes(@PathVariable Integer id) throws ResourceNotFoundException{
		return new ResponseEntity<ArrayList<AirplaneDTO>> (airplaneService.getAllAirplanes(id), HttpStatus.OK);
	}
	
	/**
	 * Vraća sve aktivne avione aviokompanije.
	 * @param id - oznaka aviokomapnije.
	 * @return - lista aviona.
	 * @throws ResourceNotFoundException - ako aviokompanija nije pronađena.
	 */
	@RequestMapping(value = "/getActiveAirplanes/{id}", method = RequestMethod.GET)
	public ResponseEntity<ArrayList<AirplaneDTO>> getActiveAirplanes(@PathVariable Integer id) throws ResourceNotFoundException{
		return new ResponseEntity<ArrayList<AirplaneDTO>> (airplaneService.getActiveAirplanes(id), HttpStatus.OK);
	}
}
