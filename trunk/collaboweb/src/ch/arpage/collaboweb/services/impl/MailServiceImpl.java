/**
 * collaboweb
 * Feb 20, 2007
 */
package ch.arpage.collaboweb.services.impl;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import ch.arpage.collaboweb.services.MailService;

/**
 * Service used to send Email messages.<br>
 * This implementation use the Spring MailSender class.
 *
 * @see org.springframework.mail.MailSender
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MailServiceImpl implements MailService {

	private MailSender mailSender;
	private String defaultFrom;
	private String defaultTo;
	
	/**
	 * Set the mailSender.
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	/**
	 * Set the defaultFrom.
	 * @param defaultFrom the defaultFrom to set
	 */
	public void setDefaultFrom(String defaultFrom) {
		this.defaultFrom = defaultFrom;
	}
	
	/**
	 * Set the defaultTo.
	 * @param defaultTo the defaultTo to set
	 */
	public void setDefaultTo(String defaultTo) {
		this.defaultTo = defaultTo;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.MailService#sendMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendMessage(String from, String to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setText(text);
		mailSender.send(msg);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.MailService#sendMessage(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendMessage(String to, String subject, String text) {
		sendMessage(defaultFrom, to, subject, text);
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.MailService#sendMessage(java.lang.String, java.lang.String)
	 */
	public void sendMessage(String subject, String text) {
		sendMessage(defaultFrom, defaultTo, subject, text);
	}
}
