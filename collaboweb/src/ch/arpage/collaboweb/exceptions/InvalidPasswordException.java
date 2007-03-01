/**
 * collaboweb
 * Feb 6, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Invalid password exception
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class InvalidPasswordException extends CollabowebException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final InvalidPasswordException INSTANCE = 
		new InvalidPasswordException();

	/**
	 * Private constructor
	 */
	private InvalidPasswordException() {
	}
	
	/**
	 * Returns the singleton version of this exception
	 * @return	The singleton version of this exception
	 */
	public static InvalidPasswordException getInstance() {
		return INSTANCE;
	}
}
