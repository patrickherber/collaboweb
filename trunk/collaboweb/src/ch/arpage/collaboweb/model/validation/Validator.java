/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * Validator interface used to validate attribute's values
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface Validator {

	/**
	 * Validate the given attribute's <code>value</code>. The validator rule
	 * can be customized passing a parameter.
	 * 
	 * @param value		The value to be validated
	 * @param parameter	The parameter for the validator rule
	 * @return			<code>true</code> if the value is valid, 
	 * 					<code>false</code> otherwise.
	 */
	boolean isValid(Object value, String parameter);

	/**
	 * Validate the given validator rule's <code>parameter</code>. 
	 * 
	 * @param parameter	The parameter for the validator rule to be validated
	 * @return			<code>true</code> if the parameter is valid, 
	 * 					<code>false</code> otherwise.
	 */
	boolean isValidParameter(String parameter);
}
