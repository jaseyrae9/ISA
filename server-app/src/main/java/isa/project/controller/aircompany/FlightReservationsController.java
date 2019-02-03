package isa.project.controller.aircompany;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.security.TokenUtils;
import isa.project.service.aircompany.FlightReservationsService;

@RestController
@RequestMapping(value = "/aircompanies")
public class FlightReservationsController {
	@Autowired
	private TokenUtils tokenUtils;
	@Autowired
	private FlightReservationsService flightReservationsService;

	/**
	 * Vrsi brzu rezervaciju karte za ulogovanog korisnika.
	 * @param id - oznaka brze karte
	 * @param request - zahtev iz koga se preuzima token (da bi se nasao ulogovani korisnik)
	 * @param passport - broj pasosa
	 * @return 
	 * @throws ResourceNotFoundException - ako nije pronadjena brza karta sa datim id
	 * @throws ReservationNotAvailable - ako je karta prodana
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/fastReservation/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> fastReservation(@PathVariable Integer id, HttpServletRequest request,
			@NotBlank(message = "Passport number must be entered.") @RequestBody String passport)
			throws ResourceNotFoundException, ReservationNotAvailable {
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		flightReservationsService.fastReserve(id, email, passport);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
