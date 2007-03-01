/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.commons.validator.GenericValidator;
import org.springframework.util.NumberUtils;

import ch.arpage.collaboweb.common.Utils;

/**
 * Validator implementation, which checks that the given value is longer 
 * than the number of characters specified by the given parameter.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MaxLengthValidator implements Validator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		return GenericValidator.maxLength((String) value, 
				Utils.parseInt(parameter));
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		try {
			Number number = NumberUtils.parseNumber(parameter, Integer.class);
			return (number != null && number.intValue() >= 0);
		} catch (Exception e) {
			return false;
		}
	}
}
