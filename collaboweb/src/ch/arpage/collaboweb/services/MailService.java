/**
 * collaboweb
 * Feb 20, 2007
 */
package ch.arpage.collaboweb.services;

/**
 * Service used to send Email messages.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface MailService {
	
	/**
	 * Sends a message with the given subject and text to the default to address.
	 * 
	 * @param subject	The subject of the email
	 * @param text		The text fo the email
	 */
	void sendMessage(String subject, String text);
	
	/**
	 * Sends a message with the given subject and text to the given to address.
	 * 
	 * @param to		The address of the email beneficiary
	 * @param subject	The subject of the email
	 * @param text		The text fo the email
	 */
	void sendMessage(String to, String subject, String text);
	
	/**
	 * Sends a message with the given subject and text to the given to address,
	 * in name of the give from email address.
	 * 
	 * @param from		The address of the email sender
	 * @param to		The address of the email beneficiary
	 * @param subject	The subject of the email
	 * @param text		The text fo the email
	 */
	void sendMessage(String from, String to, String subject, String text);
}
