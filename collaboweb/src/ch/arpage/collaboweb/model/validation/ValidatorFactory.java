/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory used to instantiate attribute validator objects.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidatorFactory {
	
	private static final Map<String, Validator> VALIDATORS = 
		new HashMap<String, Validator>();

	/**
	 * Returns the validator with the given className
	 * @param className	The name of the validator class
	 * @return the validator with the given className
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Validator getValidator(String className) 
			throws InstantiationException, IllegalAccessException, 
			ClassNotFoundException {
		Validator validator = VALIDATORS.get(className);
		if (validator == null) {
			validator = (Validator) Class.forName(className).newInstance();
			VALIDATORS.put(className, validator);
		}
		return validator;
	}
}
