/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import ch.arpage.collaboweb.model.validation.Validator;
import ch.arpage.collaboweb.model.validation.ValidatorFactory;

/**
 * ValidationType
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationType extends AbstractValidationType {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns the validator for the given validation type
	 * @return	The validator for the given validation type
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public Validator getValidator() throws InstantiationException, 
			IllegalAccessException, ClassNotFoundException {
		return ValidatorFactory.getValidator(getClassName());
	}
}
