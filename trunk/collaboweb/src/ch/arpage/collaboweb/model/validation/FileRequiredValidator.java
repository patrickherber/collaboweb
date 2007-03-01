/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.struts.upload.FormFile;

/**
 * Validator implementation, which checks that the given FormFile parameter 
 * is not empty (file size &gt; 0).
 *
 * @see org.apache.struts.upload.FormFile
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class FileRequiredValidator implements Validator {
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		if (value instanceof FormFile) {
			return ((FormFile) value).getFileSize() > 0;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.model.validation.Validator#isValidParameter(java.lang.String)
	 */
	public boolean isValidParameter(String parameter) {
		return true;
	}
}
