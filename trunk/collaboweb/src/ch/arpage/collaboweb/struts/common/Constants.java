/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.common;

/**
 * Constants for the front-end tier.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface Constants {
	
	/** The language attribute key */
	String LANGUAGE = "language";

	/** The default language */
	String DEFAULT_LANGUAGE = "en";

	/** The user object attribute key */
	String USER_INFO = "userInfo";

	/** The requested URL attribute key */
	String REQUESTED_URL = "requestedUrl";

	/** The key of the Not Authorized Page global forward */
	String NOT_AUTHORIZED_PAGE = "403";

	/** The URL Parameters attribute key */
	String URL_PARAMETERS = "urlParameters";

	/** The key for the success Forward */
	String SUCCESS_KEY = "success";

	/** The key for the failure Forward */
	String FAILURE_KEY = "failure";

	/** The clipboard attribute key */
	String CLIPBOARD = "clipboard";
	
}
