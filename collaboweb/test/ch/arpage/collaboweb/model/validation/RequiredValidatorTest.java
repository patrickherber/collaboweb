/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

import junit.framework.Assert;

/**
 * RequiredValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RequiredValidatorTest extends AbstractValidatorTest {
	
	RequiredValidator validator = new RequiredValidator();

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#testEmptyString()
	 */
	@Override
	public void testEmptyString() {
		Assert.assertFalse("Empty string is valid", 
				getValidator().isValid("", null));
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#testNullValue()
	 */
	@Override
	public void testNullValue() {
		Assert.assertFalse("Null is valid", getValidator().isValid(null, null));
	}
	
	public void testString() {
		Assert.assertTrue("String is not valid", 
				getValidator().isValid("ciao", null));
	}
	
	public void testSpace() {
		Assert.assertFalse("Space is valid", 
				getValidator().isValid(" ", null));
	}
}
