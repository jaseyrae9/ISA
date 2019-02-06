package isa.project.controller.aircompany;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.aspects.AdminAccountActiveCheck;
import isa.project.aspects.AirCompanyAdminCheck;
import isa.project.dto.aircompany.FlightDTO;
import isa.project.dto.aircompany.FlightSearchDTO;
import isa.project.dto.aircompany.FlightTicketsPriceChangeDTO;
import isa.project.dto.aircompany.TicketForFastReservationDTO;
import isa.project.dto.aircompany.CreateFastReservationTicketsDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.aircompany.Flight.FlightStatus;
import isa.project.model.aircompany.TicketForFastReservation;
import isa.project.security.TokenUtils;
import isa.project.service.aircompany.FlightService;

@RestController
@RequestMapping(value = "/aircompanies")
public class FlightController {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private FlightService flightService;
	
	@RequestMapping(value = "/searchFlights", method = RequestMethod.POST)
	public ResponseEntity<?> searchFlights(@Valid @RequestBody FlightSearchDTO searchData){
		return new ResponseEntity<>(flightService.findFlights(searchData), HttpStatus.OK);
	}

	/**
	 * Vraća informacije o letu. Admin aviokompanije moze da dobije informacije o
	 * in_progress letu svoje aviokompanije, svi ostali samo aktivni let.
	 * 
	 * @param id      - oznaka leta
	 * @param request - zahtev
	 * @return - informacije o letu
	 * @throws ResourceNotFoundException - ako let nije pronađen
	 */
	@RequestMapping(value = "/getFlight/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFlight(@PathVariable Integer id, HttpServletRequest request)
			throws ResourceNotFoundException {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return new ResponseEntity<>(flightService.getFlight(id, email), HttpStatus.OK);
	}

	/**
	 * Vraća letove. Admin aviokompanije za svoju aviokompaniju dobija i aktivne i
	 * in progress letove. Svi ostali dobijaju samo aktivne.
	 * 
	 * @param id      - oznaka leta
	 * @param page    - informacije koja stranica se traži
	 * @param request - zahtev
	 * @return - stranica sa letovima
	 */
	@RequestMapping(value = "/getFlights/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFlights(@PathVariable Integer id, Pageable page, HttpServletRequest request) {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		return new ResponseEntity<>(flightService.getFlights(id, page, email), HttpStatus.OK);
	}

	/**
	 * Dodaje novi let.
	 * 
	 * @param id
	 * @param flight
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws RequestDataException
	 */
	@RequestMapping(value = "/addFlight/{id}", method = RequestMethod.POST, consumes = "application/json")
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> createFlight(@PathVariable Integer id, @Valid @RequestBody FlightDTO flight)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.addNewFlight(id, flight), HttpStatus.CREATED);
	}

	/**
	 * Uređuje postojeći let.
	 * 
	 * @param id       - oznaka aviokompanije
	 * @param flightId - oznaka leta
	 * @param flight   - informacije u letu
	 * @return - uređeni let
	 * @throws ResourceNotFoundException - aviokompanija, let, avion ili destinacija
	 *                                   nisu pronađeni
	 * @throws RequestDataException      - datumi neispravni, status nije
	 *                                   in_progress
	 */
	@RequestMapping(value = "/editFlight/{id}/{flightId}", method = RequestMethod.PUT, consumes = "application/json")
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> editFlight(@PathVariable Integer id, @PathVariable Integer flightId,
			@Valid @RequestBody FlightDTO flight) throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.editFlight(id, flightId, flight), HttpStatus.OK);
	}

	/**
	 * Uređuje cene postojećeg let.
	 * 
	 * @param id       - oznaka aviokompanije
	 * @param flightId - oznaka leta
	 * @param info     - informacije o novim cenama
	 * @return - let čije karte imaju nove cene
	 * @throws ResourceNotFoundException - aviokompanija, let, avion ili destinacija
	 *                                   nisu pronađeni
	 * @throws RequestDataException      - datumi neispravni, status nije
	 *                                   in_progress
	 */
	@RequestMapping(value = "/changePrices/{id}/{flightId}", method = RequestMethod.PUT, consumes = "application/json")
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> changePrices(@PathVariable Integer id, @PathVariable Integer flightId,
			@Valid @RequestBody FlightTicketsPriceChangeDTO info)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.setTicketsPrices(id, flightId, info), HttpStatus.OK);
	}

	/**
	 * Menja status leta u obrisan.
	 * 
	 * @param id     - oznaka aviokompanije kojoj let pripada.
	 * @param flight - oznaka leta.
	 * @return
	 * @throws ResourceNotFoundException - ukoliko let sa datom oznakom nije
	 *                                   pornađen.
	 * @throws RequestDataException      - ako prosleđeni let nema status
	 *                                   in_progress
	 */
	@RequestMapping(value = "/deleteFlight/{id}/{flight}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> deleteFlight(@PathVariable Integer id, @PathVariable Integer flight)
			throws ResourceNotFoundException, RequestDataException {
		flightService.changeStatus(id, flight, FlightStatus.DELETED);
		return ResponseEntity.ok().build();
	}

	/**
	 * Menja status leta u aktiva.
	 * 
	 * @param id     - oznaka aviokompanije kojoj let pripada.
	 * @param flight - oznaka leta.
	 * @return
	 * @throws ResourceNotFoundException - ukoliko let sa datom oznakom nije
	 *                                   pornađen.
	 * @throws RequestDataException      - ako prosleđeni let nema status
	 *                                   in_progress
	 */
	@RequestMapping(value = "/activateFlight/{id}/{flight}", method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> activateFlight(@PathVariable Integer id, @PathVariable Integer flight)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.changeStatus(id, flight, FlightStatus.ACTIVE), HttpStatus.OK);
	}

	/**
	 * Kreira nove karte za brzu rezervaciju.
	 * @param aircomapanyId - oznaka aviokompanije
	 * @param flightId - oznaka leta
	 * @param tickets - oznaka karti
	 * @return - let sa novo kreiranim kartama
	 * @throws ResourceNotFoundException - ako neki od resursa nije pronadjen
	 * @throws RequestDataException - ako let nije aktivan ili karta nije u stanju available
	 */
	@RequestMapping(value = "/createTickets/{aircomapanyId}/{flightId}", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> createTicketsForFastReservation(@PathVariable Integer aircomapanyId,
			@PathVariable Integer flightId, @Valid @RequestBody CreateFastReservationTicketsDTO tickets)
			throws ResourceNotFoundException, RequestDataException {
		return new ResponseEntity<>(flightService.createTicketsForFastReservations(aircomapanyId, flightId,
				tickets.getTickets(), tickets.getDiscount()), HttpStatus.CREATED);
	}
	
	/**
	 * Brise postojecu kartu za brze rezervacije. Mesto se oslobadja, tako da se moze regularno rezervisati.
	 * @param aircomapanyId - oznaka aviokomapnije
	 * @param ticketId - oznaka karte
	 * @return
	 * @throws RequestDataException - resurs nije pronadjen
	 * @throws ResourceNotFoundException - karta ne pripada prosledjenoj aviokompaniji
	 */
	@RequestMapping(value = "/deleteTicket/{aircomapanyId}/{ticketId}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> deleteTicketForFastReservation(@PathVariable Integer aircomapanyId, @PathVariable Integer ticketId) throws RequestDataException, ResourceNotFoundException{
		flightService.deleteTicketForFastReservation(aircomapanyId, ticketId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Onemogućava sedište za izabrani let.
	 * @param aircomapanyId - oznaka aviokomapnije
	 * @param flightId - oznaka leta
	 * @param ticketIds - oznake karte
	 * @return
	 * @throws RequestDataException - resurs nije pronadjen
	 * @throws ResourceNotFoundException - karta ne pripada prosledjenoj aviokompaniji
	 */
	@RequestMapping(value = "/disableTicket/{aircomapanyId}/{flightId}", method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('AIRADMIN')")
	@AdminAccountActiveCheck
	@AirCompanyAdminCheck
	public ResponseEntity<?> disableTicketForReservation(@PathVariable Integer aircomapanyId, @PathVariable Integer flightId ,@RequestBody List<Long> ticketsIds) throws RequestDataException, ResourceNotFoundException{
		return new ResponseEntity<>(flightService.disableSeats(aircomapanyId, flightId, ticketsIds) ,HttpStatus.OK);
	}
	
	/**
	 * Vraca sve karte za brzu rezervaciju aviokompanije.
	 * @param aircomapanyId - oznaka kompanije
	 * @return - karte za brzu rezervaciju
	 * @throws ResourceNotFoundException - ako aviokompanija nije pronadjena
	 */
	@RequestMapping(value = "/getTickets/{aircomapanyId}", method = RequestMethod.GET)
	public ResponseEntity<?> getTicketsForFastResrvation(@PathVariable Integer aircomapanyId) throws ResourceNotFoundException{
		List<TicketForFastReservationDTO> ret = new ArrayList<>();
		for(TicketForFastReservation ticket: flightService.getTicketsForFastResrevation(aircomapanyId)) {
			ret.add(new TicketForFastReservationDTO(ticket));
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
}
 