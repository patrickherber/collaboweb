/**
 * collaboweb
 * Created on 17.02.2007
 */
package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import ch.arpage.collaboweb.exceptions.InvalidPasswordException;
import ch.arpage.collaboweb.exceptions.UnknownUserException;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.UserManager;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.LoginForm;

/**
 * Struts action used to login a user into the application.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class LoginAction extends AbstractAction {

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
		LoginForm loginForm = (LoginForm) form;
		if (loginForm.isSubmitted() && isValid(loginForm, request)) {
	        String requestedUrl = (String) WebUtils.getSessionAttribute(request, 
	        		Constants.REQUESTED_URL);
	        try {
	        	User user = userManager.login(loginForm.getCommunity(),
	        			loginForm.getEmail(), loginForm.getPassword());
	        	setUserInfo(request, user);
	        	if (requestedUrl != null) {
	        		WebUtils.setSessionAttribute(request, 
	        				Constants.REQUESTED_URL, null);
					return returnRequestedUrl(mapping, requestedUrl);
				} else {
					return mapping.findForward(Constants.SUCCESS_KEY);
				}
	        } catch (UnknownUserException e) {
				addGlobalError(request, "login.error.unknownUser");
	        } catch (InvalidPasswordException e) {
				addGlobalError(request, "login.error.invalidPassword");
	        } catch (Exception e) {
				addGlobalError(request, e);
			}
		}
		return mapping.getInputForward();
	}

	/**
	 * @param mapping
	 * @param requestedUrl
	 * @return
	 */
	private ActionForward returnRequestedUrl(ActionMapping mapping, 
			String requestedUrl) {
		ActionForward retVal = new ActionForward(requestedUrl);
		retVal.setModule(mapping.getModuleConfig().getPrefix());
		retVal.setRedirect(true);
		return retVal;
	}
	
	/**
	 * Performs a simple validation (all the fields must be filled)
	 * @param loginForm
	 * @param request
	 * @return
	 */
	private boolean isValid(LoginForm loginForm, HttpServletRequest request) {
		if (!StringUtils.hasText(loginForm.getCommunity())) {
			addGlobalError(request, "login.error.empty.community");
			return false;
		} else if (!StringUtils.hasText(loginForm.getEmail())) {
			addGlobalError(request, "login.error.empty.email");
			return false;
		} else if (!StringUtils.hasText(loginForm.getPassword())) {
			addGlobalError(request, "login.error.empty.password");
			return false;
		}
		return true;
	}
}
