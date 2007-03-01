/**
 * collaboweb
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao;

import ch.arpage.collaboweb.model.User;

/**
 * Data Access Object for accessing the users administration.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface UserDao {

	/**
	 * Returns the user matching the given identification coordinates.
	 * 
	 * @param community		The community's nickname 
	 * @param email			The email address of the user
	 * @param password		The password of the user
	 * @return				The User object
	 */
	User get(String community, String email, String password);
	
	/**
	 * Performs a logout of the given <code>user</code>.
	 * 
	 * @param user	The user who log out the application
	 */
	void logout(User user);
}
