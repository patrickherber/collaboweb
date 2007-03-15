/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ch.arpage.collaboweb.exceptions.ValidationException;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.AttributeForm;

/**
 * Struts action used to save an attribute object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class AttributeAction extends AbstractSecureAction {
	
	private ResourceTypeManager resourceTypeManager;
	
	/**
	 * Set the resourceTypeManager.
	 * @param resourceTypeManager the resourceTypeManager to set
	 */
	public void setResourceTypeManager(ResourceTypeManager resourceTypeManager) {
		this.resourceTypeManager = resourceTypeManager;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		AttributeForm attributeForm = (AttributeForm) form;
		
		try {
			if (attributeForm.isSubmitted()) {
				boolean isNew = (attributeForm.getAttributeId() == 0);
				resourceTypeManager.saveAttribute(attributeForm.getAttribute(),
						user);
				if (isNew) {
					return redirect(request, response, mapping, "new", 
							"?attributeId="+ attributeForm.getAttributeId() + 
							"&typeId=" + attributeForm.getTypeId()); 
				} else {
					return redirect(request, response, mapping, 
							Constants.SUCCESS_KEY, 
							"?typeId=" + attributeForm.getTypeId()); 
				}
			} else {
				Attribute attribute = resourceTypeManager.getAttribute(
						attributeForm.getAttributeId(), user);
				if (attribute != null) {
					attributeForm = new AttributeForm(attribute);
				}
			}
		} catch (ValidationException ve) {
			addGlobalErrors(request, ve.getErrors());
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		request.setAttribute("attributeForm", attributeForm);
		
		return mapping.getInputForward();
	}
}
