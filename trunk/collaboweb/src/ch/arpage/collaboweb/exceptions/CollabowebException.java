/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Base Exception for all application-specific Exception 
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class CollabowebException extends RuntimeException {

	/**
	 * Creates a new CollabowebException.
	 */
	public CollabowebException() {
		super();
	}

	/**
	 * Creates a new CollabowebException.
	 * @param message	The error message
	 * @param cause		The cause of the exception
	 */
	public CollabowebException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new CollabowebException.
	 * @param message	The error message
	 */
	public CollabowebException(String message) {
		super(message);
	}

	/**
	 * Creates a new CollabowebException.
	 * @param cause		The cause of the exception
	 */
	public CollabowebException(Throwable cause) {
		super(cause);
	}

}
