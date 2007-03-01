/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * MaxLenghthValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MaxLenghthValidatorTest extends AbstractValidatorTest {
	
	MaxLengthValidator validator = new MaxLengthValidator();

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
}
