package isa.project.controller.users;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.reservations.ReservationRequestDTO;
import isa.project.exception_handlers.RequestDataException;
import isa.project.exception_handlers.ReservationNotAvailable;
import isa.project.exception_handlers.ResourceNotFoundException;
import isa.project.model.users.Reservation;
import isa.project.security.TokenUtils;
import isa.project.service.users.ReservationService;

@RestController
@RequestMapping(value = "reservation")
public class ReservationContoller {
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private ReservationService reservationService;
	
	/**
	 * Kreira rezervaciju na osnovu korpe.
	 * @param reservationRequest
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws ReservationNotAvailable
	 * @throws RequestDataException
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> reserve(@Valid @RequestBody ReservationRequestDTO reservationRequest, HttpServletRequest request) throws ResourceNotFoundException, ReservationNotAvailable, RequestDataException{
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Reservation reservation = reservationService.reserve(reservationRequest, email);
		System.out.println("Kreirana");
		reservationService.sendFriendInvites(reservation);
		return ResponseEntity.ok(reservation);
	}
}
