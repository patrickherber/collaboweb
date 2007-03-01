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

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to delete a view object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewDeleteAction extends AbstractSecureAction {
	
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

		int typeId = ServletRequestUtils.getIntParameter(request, "typeId", 0);
		int viewTypeId = 
			ServletRequestUtils.getIntParameter(request, "viewTypeId", 0);
		
		if (typeId != 0 && viewTypeId != 0) {
			try {
				resourceTypeManager.deleteView(typeId, viewTypeId, user);
				addSessionMessage(request, "message.view.deleted");
				return redirect(request, response, mapping, 
						Constants.SUCCESS_KEY, "?typeId=" + typeId);
			} catch (Exception e) {
				addGlobalError(request, e);
			}
		}
		return forward(mapping, Constants.SUCCESS_KEY, "?typeId=" + typeId, 
				false);
	}
}
