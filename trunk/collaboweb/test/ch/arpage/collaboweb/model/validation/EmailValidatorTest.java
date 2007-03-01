/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

import junit.framework.Assert;

/**
 * EmailValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class EmailValidatorTest extends AbstractValidatorTest {

	EmailValidator validator = new EmailValidator();
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
	
	public void testCorrectEmail() {
		Assert.assertTrue("Correct email is not valid", validator.isValid(
				"patrick@arpage.ch", null));
	}
	
	public void testIncorrectEmail() {
		Assert.assertFalse("Incorrect email is valid", validator.isValid(
				"patrick", null));
	}
	
	public void testCorrectEmailWithUppercase() {
		Assert.assertTrue("Correct email with uppercaseis not valid", 
				validator.isValid("PATRICK@arpage.ch", null));
	}
	
	public void testCorrectEmailWith3Points() {
		Assert.assertTrue("Correct email with 3 points not valid", 
				validator.isValid("patrick.herber.2@arpage.ch", null));
	}
	
	public void testIncorrectEmailWithoutExtension() {
		Assert.assertFalse("Incorrect email without extension is valid", 
				validator.isValid("patrick@arpage", null));
	}
}
