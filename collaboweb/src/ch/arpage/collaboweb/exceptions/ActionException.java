/**
 * collaboweb
 * Feb 13, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Exception throws during the Action executions
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionException extends CollabowebException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ActionException
	 */
	public ActionException() {
		super();
	}

	/**
	 * Creates a new ActionException.
	 * @param message	The message
	 * @param cause		The cause of the exception
	 */
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new ActionException.
	 * @param message 	The message
	 */
	public ActionException(String message) {
		super(message);
	}

	/**
	 * Creates a new ActionException.
	 * @param cause		The cause of the exception
	 */
	public ActionException(Throwable cause) {
		super(cause);
	}
}
