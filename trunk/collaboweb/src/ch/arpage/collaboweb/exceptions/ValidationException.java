/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown wenn an en exception occurrs during the validation process.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationException extends CollabowebException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> errors;
	
	/**
	 * Creates a new ValidationException with the given list of error messages.
	 * @param errors	The list of error messages.
	 */
	public ValidationException(List<String> errors) {
		this.errors = errors;
	}
	
	/**
	 * Creates a new ValidationException with the given error message.
	 * @param error		The error message
	 */
	public ValidationException(String error) {
		this.errors = new ArrayList<String>(1);
		this.errors.add(error);
	}
	
	/**
	 * Returns the errors.
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}
}
