package isa.project.controller.shared;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import isa.project.dto.reservations.ReservationDTO;
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
	 * @throws MessagingException 
	 * @throws InterruptedException 
	 * @throws MailException 
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> reserve(@Valid @RequestBody ReservationRequestDTO reservationRequest, HttpServletRequest request) throws ResourceNotFoundException, ReservationNotAvailable, RequestDataException, MailException, InterruptedException, MessagingException{
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Reservation reservation = reservationService.reserve(reservationRequest, email);
		reservationService.sendFriendInvites(reservation);
		reservationService.sendMailToUser(reservation);
		return ResponseEntity.ok(new ReservationDTO(reservation));
	}
	
	
	/**
	 * Otkazuje rezervaciju.
	 * @param id
	 * @param request
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws RequestDataException
	 */
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	@RequestMapping(value = "/cancle/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> cancel(@PathVariable Integer id, HttpServletRequest request) throws ResourceNotFoundException, RequestDataException{
		String email = tokenUtils.getEmailFromToken(tokenUtils.getToken(request));
		Reservation reservation = reservationService.cancelReservation(id, email);
		return ResponseEntity.ok(new ReservationDTO(reservation));
	}
}
