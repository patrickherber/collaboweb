/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.services.impl;

import ch.arpage.collaboweb.exceptions.NotAuthorizedException;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.SecurityService;

/**
 * Simple implementation of the Security Service
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class SecurityServiceImpl implements SecurityService {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.SecureService#checkAuthenticatedUser(ch.arpage.collaboweb.model.User)
	 */
	public void checkAuthenticatedUser(User user) {
		if (user == null) {
			throw NotAuthorizedException.getInstance();
		}
	}
}
