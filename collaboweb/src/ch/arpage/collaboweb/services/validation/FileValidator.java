/**
 * collaboweb
 * Feb 6, 2007
 */
package ch.arpage.collaboweb.services.validation;

import java.util.Map;

import org.apache.struts.upload.FormFile;
import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.User;

/**
 * Validates a file object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class FileValidator implements ResourceValidator {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.validation.ResourceValidator#isValid(ch.arpage.collaboweb.model.Resource, ch.arpage.collaboweb.model.User)
	 */
	public boolean isValid(Resource resource, User user) {
		Map<Integer, ResourceAttribute> attrs = resource.getResourceAttributes();
		Attribute attribute = 
			resource.getResourceType().getAttribute("file-content");
		if (attribute != null) {
			ResourceAttribute attr = attrs.get(attribute.getAttributeId());
			if (attr != null) {
				if (attr.getValue() instanceof FormFile) {
					FormFile formFile = (FormFile) attr.getValue();
					if (formFile.getFileSize() != 0) {
						attribute = 
							resource.getResourceType().getAttribute("size");
						if (attribute != null) {
							ResourceAttribute sizeAttribute = 
								new ResourceAttribute();
							sizeAttribute.setAttributeId(attribute.getAttributeId());
							sizeAttribute.setValue(formFile.getFileSize());
							resource.addResourceAttribute(sizeAttribute);
						}
					}
					attr = resource.getResourceAttribute("name");
					if (attr != null && 
							!StringUtils.hasText((String) attr.getValue()) &&
							StringUtils.hasText(formFile.getFileName())) {
						attr.setValue(formFile.getFileName());
					}
					return (resource.getResourceId() != 0 || 
							formFile.getFileSize() > 0);
				}
			}
		}
		return (resource.getResourceId() != 0);
	}
}
