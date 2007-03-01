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

/**
 * Struts action used to save an action object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ActionAction extends AbstractSecureAction {
	
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
		
		ch.arpage.collaboweb.struts.forms.ActionForm actionForm = 
			(ch.arpage.collaboweb.struts.forms.ActionForm) form;
		
		try {
			if (actionForm.isSubmitted()) {
				resourceTypeManager.saveAction(actionForm.getAction(), user);
				return redirect(request, response, mapping, 
						Constants.SUCCESS_KEY, null);
			} else {
				if (actionForm.getActionId() != 0) {
					actionForm = new ch.arpage.collaboweb.struts.forms.ActionForm(
							resourceTypeManager.getAction(
									actionForm.getActionId(),
									user));
				}
			}
		} catch (ValidationException ve) {
			addGlobalErrors(request, ve.getErrors());
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		
		request.setAttribute("actionForm", actionForm);
		
		return mapping.getInputForward();
	}
}
