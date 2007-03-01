/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.services.impl;

import org.springframework.dao.DataRetrievalFailureException;

import ch.arpage.collaboweb.dao.UserDao;
import ch.arpage.collaboweb.exceptions.UnknownUserException;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.UserManager;

/**
 * Implementation of the User Manager Service.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UserManagerImpl implements UserManager {
	
	private UserDao userDao;
	
	/**
	 * Set the userDao.
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.UserManager#login(java.lang.String, java.lang.String, java.lang.String)
	 */
	public User login(String community, String email, String password) {
		try {
			return userDao.get(community, email, password);
		} catch (DataRetrievalFailureException e) {
			throw UnknownUserException.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.UserManager#logout(ch.arpage.collaboweb.model.User)
	 */
	public void logout(User user) {
		if (user != null) {
			userDao.logout(user);
		}
	}

}
