/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * AbstractValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractValidatorTest extends TestCase {

	protected abstract Validator getValidator();
	
	public void testNullValue() {
		Assert.assertTrue("Null is not valid", 
				getValidator().isValid(null, null));
	}
	
	public void testEmptyString() {
		Assert.assertTrue("Empty String is not valid", 
				getValidator().isValid("", null));
	}
	
}
