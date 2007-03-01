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
import ch.arpage.collaboweb.struts.forms.ViewForm;

/**
 * Struts action used to save a view object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewAction extends AbstractSecureAction {
	
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
		
		ViewForm viewForm = (ViewForm) form;
		
		try {
			if (viewForm.isSubmitted()) {
				resourceTypeManager.saveView(viewForm.getView(), user);
				return redirect(request, response, mapping, Constants.SUCCESS_KEY, 
						"?typeId=" + viewForm.getTypeId());
			} else {
				if (viewForm.getTypeId() != 0 && viewForm.getViewTypeId() != 0) {
					viewForm = new ViewForm(resourceTypeManager.getView(
							viewForm.getTypeId(), viewForm.getViewTypeId(), user));
				}
			}
		} catch (ValidationException ve) {
			addGlobalErrors(request, ve.getErrors());
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		addViewTypesAttribute(user, request);
		request.setAttribute("viewForm", viewForm);
		return mapping.getInputForward();
		
	}

	/**
	 * Adds the view types, that the current user can see, as attribute to the
	 * request.
	 * @param user		The current user
	 * @param request	The request
	 */
	private void addViewTypesAttribute(User user, HttpServletRequest request) {
		request.setAttribute("viewTypes", 
				resourceTypeManager.getViewTypes(user));
	}
}
