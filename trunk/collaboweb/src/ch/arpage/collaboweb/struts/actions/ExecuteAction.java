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
import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.exceptions.ActionException;
import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ActionManager;
import ch.arpage.collaboweb.services.ResourceTypeManager;

/**
 * Struts action used to execute an action on one or more resource(s)
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ExecuteAction extends AbstractSecureAction {
	
	private ActionManager actionManager;
	private ResourceTypeManager resourceTypeManager;
	
	/**
	 * Set the actionManager.
	 * @param actionManager the actionManager to set
	 */
	public void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}
	
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

		int actionId = 
			ServletRequestUtils.getIntParameter(request, "actionId", 0);
		
		if (actionId != 0) {
			try {
				Action action = resourceTypeManager.getAction(actionId, user);
				String forward = actionManager.executeAction(action.getClassName(), 
						request, response, user, action.getParameter());
				if (forward != null) {
					return mapping.findForward(forward);
				} else {
					return null;
				}
			} catch (ActionException e) {
				addSessionError(request, e.getMessage(), e);
			} catch (Exception e) {
				addSessionError(request, e);
			}
		}
		
		return redirectToReferer(request, response, mapping);
	}
}
