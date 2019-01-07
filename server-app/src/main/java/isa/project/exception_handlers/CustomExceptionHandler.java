package isa.project.exception_handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles errors triggered by @Valid annotation and builds response for them.
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.springframework.web.bind.MethodArgumentNotValidException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorResponse error = new ErrorResponse("Validation Failed", details);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles errors when requested resource is not found and builds error response for them.
	 * @param ex
	 * @return custom error response
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponse response = new ErrorResponse("Resource not found", details);
		return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
	}
	
	/**
	 * Handles errors when data sent in request is incorrect.
	 * @param ex
	 * @return custom error response
	 */
	@ExceptionHandler(RequestDataException.class)
	public ResponseEntity<Object> requestDataNotValid(RequestDataException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponse response = new ErrorResponse("Request error", details);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles errors triggered by database, such as violation of unique constrain.
	 * @param ex
	 * @return custom error response
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> dataIntegrity(ResourceNotFoundException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponse response = new ErrorResponse("Constrain violation.", details);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles errors triggered by bad credentials.
	 * @param ex
	 * @return custom error response
	 */
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> badCredentials(BadCredentialsException ex) {
		List<String> details = new ArrayList<>();
		details.add("Password or email are incorrect.");
		ErrorResponse response = new ErrorResponse("Bad credentials", details);
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handles errors triggered by admin accounts.
	 * @param ex
	 * @return custom error response
	 */
	@ExceptionHandler(AdminAccountException.class)
	public ResponseEntity<Object> adminAccountExceptionHandler(AdminAccountException ex) {
		List<String> details = new ArrayList<>();
		details.add(ex.getMessage());
		ErrorResponse response = new ErrorResponse("Admin account exception", details);
		return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
	}
}
