/**
 * collaboweb5
 * Dec 10, 2006
 */
package ch.arpage.collaboweb.dao.test;

import ch.arpage.collaboweb.dao.UserDao;
import ch.arpage.collaboweb.exceptions.InvalidPasswordException;
import ch.arpage.collaboweb.exceptions.UnknownUserException;
import ch.arpage.collaboweb.model.Community;
import ch.arpage.collaboweb.model.User;

/**
 * UserDaoImpl
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UserDaoImpl implements UserDao {

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.UserDao#get(java.lang.String, java.lang.String)
	 */
	public User get(String community, String email) {
		if (isValid(community, email)) {
			return createUser(community, email);
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.UserDao#get(java.lang.String, java.lang.String, java.lang.String)
	 */
	public User get(String community, String email, String password) {
		if (isValid(community, email)) {
			if ("1234".equals(password)) {
				return createUser(community, email);
			} else {
				throw InvalidPasswordException.getInstance();
			}
		} else {
			throw UnknownUserException.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.dao.UserDao#logout(ch.arpage.collaboweb.model.User)
	 */
	public void logout(User user) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param community
	 * @param email
	 * @return
	 */
	private boolean isValid(String community, String email) {
		return ("test".equals(community) && "patrick@arpage.ch".equals(email));
	}

	/**
	 * @param nickname
	 * @param email
	 * @return
	 */
	private User createUser(String nickname, String email) {
		User user = new User();
		user.setEmail(email);
		user.setCommunity(createCommunity(nickname));
		user.setFirstName("Patrick");
		user.setLanguage("en");
		user.setLastName("Herber");
		user.setResourceId(100L);
		return user;
	}

	/**
	 * @param nickname
	 * @return
	 */
	private Community createCommunity(String nickname) {
		Community community = new Community();
		community.setCommunityId(1L);
		community.setName(nickname + " Community");
		community.setNickname(nickname);
		return community;
	}
}
