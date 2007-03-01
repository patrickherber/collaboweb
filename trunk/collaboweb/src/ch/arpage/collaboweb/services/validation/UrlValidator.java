/**
 * collaboweb
 * Feb 21, 2007
 */
package ch.arpage.collaboweb.services.validation;

import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.User;

/**
 * Validates an URL object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UrlValidator implements ResourceValidator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.ResourceValidator#isValid(ch.arpage.collaboweb.model.Resource, ch.arpage.collaboweb.model.User)
	 */
	public boolean isValid(Resource resource, User user) {
		Attribute attribute = 
			resource.getResourceType().getAttribute("url");
		if (attribute != null) {
			ResourceAttribute attr = 
				resource.getResourceAttribute(attribute.getAttributeId());
			if (attr != null) {
				String value = (String) attr.getValue();
				if (StringUtils.hasText(value)) {
					attr = resource.getResourceAttribute("name");
					if (attr != null && 
							!StringUtils.hasText((String) attr.getValue())) {
						attr.setValue(value);
					}
				}
			}
		}
		return true;
	}
}
