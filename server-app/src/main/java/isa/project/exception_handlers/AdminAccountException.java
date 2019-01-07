package isa.project.exception_handlers;

/**
 * Modeluje izuzetak nastao ako admin pokušava da koristi nalog pre prve promene
 * šifre ili kada pokušava da administrira kompaniju kojoj nije dodelje.
 * 
 * @author Milica
 *
 */
public class AdminAccountException extends Exception {
	private static final long serialVersionUID = 5154608636126896223L;
	public AdminAccountException(String string) {
		super(string);
	}
}
