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
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.ValidationForm;

/**
 * Struts action used to save an attribute validation object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationAction extends AbstractSecureAction {
	
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
		
		ValidationForm validationForm = (ValidationForm) form;
		
		try {
			if (validationForm.isSubmitted()) {
				try {
					resourceTypeManager.saveValidation(
							validationForm.getValidation(), user);
					return redirect(request, response, mapping, 
							Constants.SUCCESS_KEY,
							"?attributeId=" + validationForm.getAttributeId());
				} catch (ValidationException ve) {
					addGlobalErrors(request, ve.getErrors());
					addValidationTypesAttribute(user, request);
				}
			} else {
				if (validationForm.getValidationTypeId() != 0) {
					validationForm = new ValidationForm(
							resourceTypeManager.getValidation(
									validationForm.getAttributeId(),
									validationForm.getValidationTypeId(),
									user));
				}
				addValidationTypesAttribute(user, request);
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		request.setAttribute("validationForm", validationForm);
		return mapping.getInputForward();
		
	}

	/**
	 * @param user
	 * @param request
	 */
	private void addValidationTypesAttribute(User user, HttpServletRequest request) {
		request.setAttribute("validationTypes",
				resourceTypeManager.getValidationTypes(user));
	}
}
