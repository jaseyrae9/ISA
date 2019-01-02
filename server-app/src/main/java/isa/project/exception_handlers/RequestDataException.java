package isa.project.exception_handlers;

/**
 * Modeluje izuzetak nastao jer podaci prosleđeni unutar zahteva nisu ispravni.
 * Primer upotrebe: pokušaj slanja zahteva za prijateljstvo samom sebi.
 * 
 * @author Milica
 *
 */
public class RequestDataException extends Exception {
	private static final long serialVersionUID = 8917082872775942972L;

	/**
	 * Kraira novi izuzetak.
	 * 
	 * @param message - poruka koja opisuje izuzetak.
	 */
	public RequestDataException(String message) {
		super(message);
	}
}
