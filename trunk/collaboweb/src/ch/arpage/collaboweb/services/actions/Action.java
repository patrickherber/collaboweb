/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.services.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.arpage.collaboweb.model.User;

/**
 * Interface for actions to be executed on a resource type.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface Action {
	
	/**
	 * Executes the action.
	 * @param request	The request
	 * @param response	The response
	 * @param user		The current user
	 * @param parameter	The action parameter
	 * @return			The ID of next page to be displayed or null if no page
	 * 					has to be displayed
	 * @throws Exception
	 */
	String execute(HttpServletRequest request, HttpServletResponse response, 
			User user, String parameter) throws Exception;
}
