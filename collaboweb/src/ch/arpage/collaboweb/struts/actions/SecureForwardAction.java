package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * <p>An <strong>Action</strong> that forwards to the context-relative
 * URI specified by the <code>parameter</code> property of our associated
 * <code>ActionMapping</code>.  This can be used to integrate Struts with
 * other business logic components that are implemented as servlets (or JSP
 * pages), but still take advantage of the Struts controller servlet's
 * functionality (such as processing of form beans).</p>
 *
 * <p>To configure the use of this Action in your
 * <code>struts-config.xml</code> file, create an entry like this:</p>
 *
 * <code>
 *   &lt;action path="/saveSubscription"
 *           type="ch.arpage.collaboweb.struts.actions.SecureForwardAction"
 *           name="subscriptionForm"
 *          scope="request"
 *          input="/subscription.jsp"
 *      parameter="/path/to/processing/servlet"/&gt;
 * </code>
 *
 * <p>which will forward control to the context-relative URI specified by the
 * <code>parameter</code> attribute.</p>
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 * 
 * Created on 21.09.2003
 */
public class SecureForwardAction extends AbstractSecureAction {

	/**
	 * The message resources for this package.
	 */
	protected static MessageResources messages = 
		MessageResources.getMessageResources("org.apache.struts.actions.LocalStrings");

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {	
		
		// Create a RequestDispatcher the corresponding resource
		String path = mapping.getParameter();

		if (path == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					messages.getMessage("forward.path"));
			return (null);
		}
		
		String urlParameters = (String) WebUtils.getSessionAttribute(request, 
				Constants.URL_PARAMETERS);
		if (StringUtils.hasText(urlParameters)) {
			path += ((path.indexOf('?') == -1) ? '?' : '&') + urlParameters; 
			WebUtils.setSessionAttribute(request, Constants.URL_PARAMETERS, null);
		}
		// Let the controller handle the request
		ActionForward retVal = new ActionForward(path);
		retVal.setModule(mapping.getModuleConfig().getPrefix());
		return retVal;
	}
}

