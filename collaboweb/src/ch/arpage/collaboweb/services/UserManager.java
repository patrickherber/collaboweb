/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services;

import ch.arpage.collaboweb.model.User;

/**
 * Service used for the user management
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface UserManager {

	User login(String community, String email, String password);
	
	void logout(User user);
}
