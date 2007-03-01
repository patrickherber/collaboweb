/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * Validator implementation, which checks that the given value is not empty 
 * (null or zero characters long)
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RequiredValidator implements Validator {
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		if (value != null) {
			if (value instanceof String) {
				return ((String) value).trim().length() > 0;
			}
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		return true;
	}
}
