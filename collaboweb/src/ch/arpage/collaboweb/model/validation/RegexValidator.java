/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;
import org.springframework.util.StringUtils;

/**
 * Validator implementation, which checks that the given value match the regular
 * expression given with the given parameter.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RegexValidator implements Validator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		return GenericValidator.matchRegexp((String) value, parameter);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		try {
			return (StringUtils.hasText(parameter) && 
					Pattern.compile(parameter) != null);
		} catch (Exception e) {
			return false;
		}
	}
}
