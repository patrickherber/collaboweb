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

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.ValidationTypeForm;

/**
 * Struts action used to save an attribute validation type object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ValidationTypeAction extends AbstractSecureAction {
	
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
		
		ValidationTypeForm validationTypeForm = (ValidationTypeForm) form;
		
		try {
			if (validationTypeForm.isSubmitted()) {
				resourceTypeManager.saveValidationType(
						validationTypeForm.getValidationType(), user);
				return mapping.findForward(Constants.SUCCESS_KEY);
			} else {
				if (validationTypeForm.getValidationTypeId() != 0) {
					validationTypeForm = new ValidationTypeForm(
							resourceTypeManager.getValidationType(
									validationTypeForm.getValidationTypeId(),
									user));
				}
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		
		request.setAttribute("validationTypeForm", validationTypeForm);
		
		return mapping.getInputForward();
		
	}
}
