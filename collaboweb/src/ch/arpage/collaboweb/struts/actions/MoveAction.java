/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to move the objects stored in the clipboard under a new
 * target object.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MoveAction extends AbstractClipboardAction {
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		boolean redirect = false;
		
		long resourceId = 
			ServletRequestUtils.getLongParameter(request, "resourceId", 0);
		
		if (resourceId != 0L) {
			Set<Long> clipboard = getSessionClipboard(request, false);
			if (clipboard != null && !clipboard.isEmpty()) {
				try {
					resourceManager.move(clipboard, resourceId, user);
					emptyClipboard(request);
					redirect = true;
				} catch (Exception e) {
					addGlobalError(request, e);
				}
			}
		}
		
		return forward(mapping, Constants.SUCCESS_KEY, 
				"?resourceId=" + resourceId, redirect);
	}
}
