/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * MinLengthValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MinLengthValidatorTest extends AbstractValidatorTest {
	
	MinLengthValidator validator = new MinLengthValidator();

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
}
