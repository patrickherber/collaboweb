/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.exceptions;

/**
 * Not authorized exception
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class NotAuthorizedException extends CollabowebException {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private static final NotAuthorizedException INSTANCE = 
		new NotAuthorizedException();

	/**
	 * Private constructor
	 */
	private NotAuthorizedException() {
	}

	/**
	 * Returns the singleton version of this exception
	 * @return	The singleton version of this exception
	 */
	public static NotAuthorizedException getInstance() {
		return INSTANCE;
	}
}
