/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.commons.validator.GenericValidator;
import org.springframework.util.StringUtils;

/**
 * Validator implementation, which checks that the given parameter has an URL 
 * syntax.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UrlValidator implements Validator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		String sValue = (String) value;
		if (StringUtils.hasText(sValue)) {
			return GenericValidator.isUrl(sValue);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		return true;
	}
}
