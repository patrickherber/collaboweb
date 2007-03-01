/**
 * collaboweb7
 * Feb 9, 2007
 */
package ch.arpage.collaboweb.model.validation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

/**
 * DateValidationTest
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class DateValidationTest extends AbstractValidatorTest {

	DateValidator validator = new DateValidator();
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.AbstractValidatorTest#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}
	
	public void testString() {
		Assert.assertFalse("String is not valid", 
				validator.isValid("test", null));
	}
	
	public void testShortDate() {
		Assert.assertTrue("Short date is not valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(
						new Date()), null));
	}
	
	public void testLongDate() {
		Assert.assertFalse("Long date is valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG).format(
						new Date()), null));
	}
	
	public void testShortDateEnglish() {
		Assert.assertTrue("English Short date is not valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, 
						Locale.ENGLISH).format(new Date()), null));
	}
	
	public void testShortDateFrench() {
		Assert.assertTrue("French Short date is not valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, 
						Locale.FRENCH).format(new Date()), null));
	}
	
	public void testShortDateGerman() {
		Assert.assertTrue("German Short date is not valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, 
						Locale.GERMAN).format(new Date()), null));
	}
	
	public void testShortDateItalian() {
		Assert.assertTrue("Italian Short date is not valid", validator.isValid(
				SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, 
						Locale.ITALIAN).format(new Date()), null));
	}
	
	public void testShortDateWithSpaces() {
		Assert.assertTrue("Short date with spaces is not valid", 
				validator.isValid(" " + SimpleDateFormat.getDateInstance(
						SimpleDateFormat.SHORT).format(new Date()) + " ", null));
	}
}
