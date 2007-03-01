/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.text.MessageFormat;

import ch.arpage.collaboweb.services.validation.ResourceValidator;
import ch.arpage.collaboweb.services.validation.ResourceValidatorManager;

/**
 * Resource validation type model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationType extends AbstractValidationType {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public ResourceValidator getValidator() {
		return ResourceValidatorManager.getResourceValidator(getClassName());
	}

	/**
	 * Validates the given resource.
	 * @param resource	The resource to be valided
	 * @param user		The current user
	 * @return			An error message if an error occurs or null if all is OK
	 */
	public String validate(Resource resource, User user) {
		if (!getValidator().isValid(resource, user)) {
			return MessageFormat.format(getMessage(user.getLanguage()), 
					(Object[]) null);
		}
		return null;
	}
}
