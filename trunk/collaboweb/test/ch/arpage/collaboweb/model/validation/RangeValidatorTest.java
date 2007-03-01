/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * RangeValidatorTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class RangeValidatorTest extends AbstractValidatorTest {
	
	RangeValidator validator = new RangeValidator();

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
}
