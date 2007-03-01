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
 * Struts action used to display the home page.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class HomeAction extends AbstractAction {

	protected ActionForward executeAction(ActionMapping mapping, 
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward(Constants.SUCCESS_KEY);
	}

}
