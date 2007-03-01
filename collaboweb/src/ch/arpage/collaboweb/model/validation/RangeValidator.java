/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.commons.validator.GenericValidator;

import ch.arpage.collaboweb.common.Utils;

/**
 * Validator implementation, which checks that the given value is between 
 * the range specified by the given parameter.<br>
 * Example of range is: "10,20"
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RangeValidator implements Validator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		double[] range = parseRange(parameter);
		return GenericValidator.isInRange(Utils.parseDouble(value), 
				range[0], range[1]);
	}
	
	public double[] parseRange(String parameter) {
		double[] range = new double[2];
		if (parameter != null) {
			String[] values = parameter.split("[ -/,;]");
			if (values.length > 0) {
				range[0] = Utils.parseDouble(values[0]);
				if (values.length > 1) {
					range[1] = Utils.parseDouble(values[1]);
				} else {
					range[1] = range[0];
				}
			}
		}
		return range;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		return true;
	}
}
