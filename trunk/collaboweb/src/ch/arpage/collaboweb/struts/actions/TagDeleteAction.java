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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to delete a tag from a resource
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class TagDeleteAction extends AbstractResourceAction {

	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		String tag = request.getParameter("tag");
		long resourceId = 
			ServletRequestUtils.getLongParameter(request, "resourceId", 0L);
		if (resourceId != 0 && StringUtils.hasText(tag)) {
			try {
				resourceManager.deleteTag(resourceId, tag, user);
			} catch (Exception e) {
				addGlobalError(request, e);
			}
		}
		return mapping.findForward(Constants.SUCCESS_KEY);
	}
}
