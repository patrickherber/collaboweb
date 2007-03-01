/**
 * collaboweb
 * Feb 17, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.UserManager;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to logout a user from the application
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class LogoutAction extends AbstractAction {

	UserManager userManager;
	
	/**
	 * Set the userManager.
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.struts.actions.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeAction(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

    	HttpSession session = request.getSession();
    	if (session != null) {
    		User user = (User) session.getAttribute(Constants.USER_INFO);
			if (user != null) {
		        try {
		        	userManager.logout(user);
		        } catch (Exception e) {
					addGlobalError(request, e);
				}
			}
			session.removeAttribute(Constants.USER_INFO);
			session.invalidate();
    	}
		return mapping.findForward(Constants.SUCCESS_KEY);
	}
}
