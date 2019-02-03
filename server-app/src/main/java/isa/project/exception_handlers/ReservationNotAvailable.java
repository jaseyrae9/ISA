package isa.project.exception_handlers;

public class ReservationNotAvailable extends Exception {
	private static final long serialVersionUID = -5839491744280099819L;
	
	public ReservationNotAvailable(String message) {
		super(message);
	}

}
