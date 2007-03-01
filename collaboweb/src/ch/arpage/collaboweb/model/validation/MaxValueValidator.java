/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.commons.validator.GenericValidator;
import org.springframework.util.NumberUtils;

import ch.arpage.collaboweb.common.Utils;

/**
 * Validator implementation, which checks that the given value is bigger
 * than the number specified by the given parameter.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MaxValueValidator implements Validator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		return GenericValidator.maxValue(Utils.parseDouble(value), 
				Utils.parseDouble(parameter));
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		try {
			Number number = NumberUtils.parseNumber(parameter, Double.class);
			return (number != null);
		} catch (Exception e) {
			return false;
		}
	}
}
