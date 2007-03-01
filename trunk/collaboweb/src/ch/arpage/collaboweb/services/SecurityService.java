/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.services;
import ch.arpage.collaboweb.model.User;

/**
 * Service used for security tasks
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface SecurityService {
	
	/**
	 * Checks that the given user is authenticated
	 * @param user	The current user
	 */
	void checkAuthenticatedUser(User user);
}
