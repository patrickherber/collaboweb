/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

/**
 * FileRequiredValidatorTest
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class FileRequiredValidatorTest extends AbstractValidatorTest {
	
	FileRequiredValidator validator = new FileRequiredValidator();

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
}
