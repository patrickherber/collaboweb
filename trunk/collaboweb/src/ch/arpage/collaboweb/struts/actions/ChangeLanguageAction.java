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

import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Struts action used to change the current language
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ChangeLanguageAction extends AbstractAction {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		String language = request.getParameter(Constants.LANGUAGE);
		if (language != null) {
			setLanguage(request, language);
		}
		
		return redirectToReferer(request, response, mapping);
	}
}
