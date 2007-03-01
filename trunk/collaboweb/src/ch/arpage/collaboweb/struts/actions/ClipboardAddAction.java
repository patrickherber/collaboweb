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

/**
 * Struts action used to add one or more resourceId(s) into the clipboard.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ClipboardAddAction extends AbstractClipboardAction {
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		long[] resourceIds = 
			ServletRequestUtils.getLongParameters(request, "resourceId");
		if (resourceIds != null && resourceIds.length > 0) {
			Set<Long> clipboard = getSessionClipboard(request, true);
			for (long resource : resourceIds) {
				clipboard.add(resource);
			}
		}
		return redirectToReferer(request, response, mapping);
	}
}
