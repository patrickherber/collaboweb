/**
 * collaboweb
 * Feb 6, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Unknown user exception
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UnknownUserException extends CollabowebException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final UnknownUserException INSTANCE = 
		new UnknownUserException();

	/**
	 * Private constructor
	 */
	private UnknownUserException() {
	}

	/**
	 * Returns the singleton version of this exception
	 * @return	The singleton version of this exception
	 */
	public static UnknownUserException getInstance() {
		return INSTANCE;
	}
}
