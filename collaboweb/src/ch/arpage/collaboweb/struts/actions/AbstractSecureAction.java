package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Superclass of all struts actions who need to be called by authenticated users.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractSecureAction extends AbstractAction {
	
    /**
	 * Checks that the user object is stored in the session. In case let the
	 * subclass execute its action, otherwise redirect to the login page.
     * @see ch.arpage.collaboweb.struts.actions.AbstractAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	User userInfo = getUserInfo(request);
		if (userInfo != null) {
			return super.execute(mapping, form, request, response);
		} else {
			saveLastUrl(request);
		}
		return mapping.findForward(Constants.NOT_AUTHORIZED_PAGE);	
    }
    
    /* (non-Javadoc)
     * @see ch.arpage.collaboweb.struts.actions.AbstractAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward executeAction(ActionMapping mapping, 
    		ActionForm form, HttpServletRequest request, 
    		HttpServletResponse response) throws Exception {
    	return executeAction(mapping, form, getUserInfo(request), 
    			request, response);
    }

	/**
	 * Execute the action.
	 * @param mapping	The action mapping
	 * @param form		The action form
	 * @param user		The current user
	 * @param request	The request
	 * @param response	The response
	 * @return			The target action forward
	 */
	protected abstract ActionForward executeAction(ActionMapping mapping, 
			ActionForm form, User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception;

	/**
	 * Save the last requested URL
	 * @param request	The request
	 */
	private void saveLastUrl(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		StringBuffer sb = request.getRequestURL();
		if (sb != null) {
			String url = sb.append((request.getQueryString() != null) ? 
					'?' + request.getQueryString() : "").toString();
			url = url.substring(url.lastIndexOf('/'), url.length());
			session.setAttribute(Constants.REQUESTED_URL, url);
		}
	}
}
