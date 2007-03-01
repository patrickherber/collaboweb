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
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to navigate through the tag-clouds.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class TagViewAction extends AbstractResourceAction {

	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		String[] tags = request.getParameterValues("tag");
		try {
			request.setAttribute("tagCloud", 
					resourceManager.getTagCloud(tags, user));
			if (tags != null && tags.length > 0) {
				request.setAttribute("list", 
						resourceManager.getTagList(tags, user));
				request.setAttribute("tags", tags);
				request.setAttribute("tagString", getTagsQueryString(tags));
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		return mapping.findForward(Constants.SUCCESS_KEY);
	}

	/**
	 * @param tags
	 * @return
	 */
	private String getTagsQueryString(String[] tags) {
		StringBuffer tagBuffer = new StringBuffer();
		for (int i = 0; i < tags.length; i++) {
			tagBuffer.append("tag=").append(tags[i]).append("&amp;");
		}
		return tagBuffer.toString();
	}
}
