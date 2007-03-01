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
import ch.arpage.collaboweb.struts.forms.ResourceTypeForm;

/**
 * Struts action used to save a resource type.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTypeAction extends AbstractSecureAction {
	
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
		
		ResourceTypeForm resourceTypeForm = (ResourceTypeForm) form;
		
		try {
			if (resourceTypeForm.isSubmitted()) {
				resourceTypeManager.save(
						resourceTypeForm.getResourceType(), user);
				if (resourceTypeForm.getTypeId() != 0) {
					resourceTypeManager.setSupportedChildren(
							resourceTypeForm.getTypeId(), 
							resourceTypeForm.getSupportedChildrenIds(), user);
				}
				return mapping.findForward(Constants.SUCCESS_KEY);
			} else {
				if (resourceTypeForm.getTypeId() != 0) {
					resourceTypeForm = new ResourceTypeForm(
							resourceTypeManager.get(
									resourceTypeForm.getTypeId(), user));
				}
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}

		request.setAttribute("resourceTypes", 
				resourceTypeManager.getList(user));
		request.setAttribute("resourceTypeForm", resourceTypeForm);
		
		return mapping.getInputForward();
		
	}
}
