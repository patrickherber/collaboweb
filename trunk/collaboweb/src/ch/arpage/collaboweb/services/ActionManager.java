/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.arpage.collaboweb.model.User;

/**
 * Service used to execute an action on a resource.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ActionManager {
	
	/**
	 * Executes the action identified by the given <code>actionName</code>.
	 * 
	 * @param actionName	The name of the action
	 * @param request		The request
	 * @param response		The response
	 * @param user			The current user
	 * @param parameter		The action parameter
	 * @return				The ID of next page to be displayed or null if no
	 * 						page has to be dispayed
	 * @throws Exception
	 */
	String executeAction(String actionName, HttpServletRequest request,
			HttpServletResponse response, User user, String parameter) 
			throws Exception;
}
