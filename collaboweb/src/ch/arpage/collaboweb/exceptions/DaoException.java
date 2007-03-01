/**
 * collaboweb
 * Feb 13, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Exception throws by Data Access Object components
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class DaoException extends CollabowebException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new DaoException.
	 */
	public DaoException() {
		super();
	}

	/**
	 * Creates a new DaoException.
	 * @param message	The error message
	 * @param cause		The cause of the exception
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new DaoException.
	 * @param message	The error message
	 */
	public DaoException(String message) {
		super(message);
	}

	/**
	 * Creates a new DaoException.
	 * @param cause		The cause of the exception
	 */
	public DaoException(Throwable cause) {
		super(cause);
	}
}
