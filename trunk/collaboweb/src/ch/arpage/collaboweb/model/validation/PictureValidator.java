/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.model.validation;

import org.apache.struts.upload.FormFile;

/**
 * Validator implementation, which checks that the given FormFile parameter 
 * is a picture.
 *
 * @see org.apache.struts.upload.FormFile
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class PictureValidator implements Validator {
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.Validator#isValid(java.lang.Object, java.lang.Object)
	 */
	public boolean isValid(Object value, String parameter) {
		if (value instanceof FormFile) {
			String contentType = ((FormFile) value).getContentType();
			return (contentType != null 
					&& contentType.toLowerCase().indexOf("image") != -1
					&& contentType.toLowerCase().indexOf("tif") == -1);
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
