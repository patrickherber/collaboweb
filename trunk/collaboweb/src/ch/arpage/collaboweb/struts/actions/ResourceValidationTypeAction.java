/**
 * collaboweb
 * Feb 17, 2007
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
import ch.arpage.collaboweb.struts.forms.ResourceValidationTypeForm;

/**
 * Struts action used to save a resource validation type object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceValidationTypeAction extends AbstractSecureAction {
	
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
		
		ResourceValidationTypeForm validationTypeForm = 
			(ResourceValidationTypeForm) form;
		
		try {
			if (validationTypeForm.isSubmitted()) {
				resourceTypeManager.saveResourceValidationType(
						validationTypeForm.getResourceValidationType(), user);
				return mapping.findForward(Constants.SUCCESS_KEY);
			} else {
				if (validationTypeForm.getValidationTypeId() != 0) {
					validationTypeForm = new ResourceValidationTypeForm(
							resourceTypeManager.getResourceValidationType(
									validationTypeForm.getValidationTypeId(),
									user));
				}
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		
		request.setAttribute("resourceValidationTypeForm", validationTypeForm);
		
		return mapping.getInputForward();
		
	}
}
