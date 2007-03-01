/**
 * collaboweb
 * Created on 17.02.2007
 */
package ch.arpage.collaboweb.services.actions;

import info.ineighborhood.cardme.VCard;
import info.ineighborhood.cardme.impl.AddressLabelImpl;
import info.ineighborhood.cardme.impl.EmailAddressImpl;
import info.ineighborhood.cardme.impl.MailingAddressImpl;
import info.ineighborhood.cardme.impl.PhoneNumberImpl;
import info.ineighborhood.cardme.impl.VCardImpl;
import info.ineighborhood.cardme.types.EmailAddressType;
import info.ineighborhood.cardme.types.EncodingType;
import info.ineighborhood.cardme.types.MailingAddressType;
import info.ineighborhood.cardme.types.PhoneNumberType;
import info.ineighborhood.cardme.types.Type;

import java.util.Calendar;

import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.common.Utils;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;

/**
 * Class used to create a VCard Object for a contact/user object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class VCardWriter {
	
	public static final String CONTENT_TYPE = "text/x-vcard";

	/**
	 * Creates a vCard for the given user/contact.
	 * @param user	The user/contact
	 * @return		A string value containing the vCard
	 */
	public static String getVCard(Resource user) throws Exception {
		VCard vcard = new VCardImpl();
		QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
		vcard.setVersion(VCard.VERSION_2_1);
		vcard.setFirstName(getAttributeValue(user, "first-name", ""));
		if (StringUtils.hasText(user.getName())) {
			vcard.setLastName(user.getName());
		} else {
			vcard.setLastName("");
		}
		vcard.setOrganization(getAttributeValue(user, "company", ""));
		vcard.setOrganizationalUnit(getAttributeValue(user, "department", ""));
		vcard.setRole(getAttributeValue(user, "position", ""));
		
		MailingAddressImpl address = new MailingAddressImpl();
		address.addType(MailingAddressType.WORK_MAILING_ADDRESS);
		address.setCountry(getAttributeValue(user, "country", ""));
		address.setLocality(getAttributeValue(user, "locality", ""));
		address.setStreetAddress(getAttributeValue(user, "address", ""));
		address.setPostalCode(getAttributeValue(user, "zip-code", ""));
		
		AddressLabelImpl addressLabel = new AddressLabelImpl();
		addressLabel.setEncodingType(EncodingType.QUOTED_PRINTABLE);
		addressLabel.addType(MailingAddressType.WORK_MAILING_ADDRESS);
		StringBuffer labelText = new StringBuffer();
		String value = getAttributeValue(user, "address", null);
		if (value != null) {
			labelText.append(value).append('\n');
		}
		value = getAttributeValue(user, "zip-code", null);
		if (value != null) {
			labelText.append(value).append(' ');
		}
		value = getAttributeValue(user, "locality", null);
		if (value != null) {
			labelText.append(value).append('\n');
		}
		labelText.append(getAttributeValue(user, "country", ""));
		addressLabel.setLabelText(codec.encode(labelText.toString()));

		address.setLabel(addressLabel);
		vcard.addAddress(address);
		
		addPhone(getAttributeValue(user, "phone-work", null), PhoneNumberType.WORK_PHONE, vcard);
		addPhone(getAttributeValue(user, "phone-home", null), PhoneNumberType.HOME_PHONE, vcard);
		addPhone(getAttributeValue(user, "mobile", null), PhoneNumberType.CELL_PHONE, vcard);
		addPhone(getAttributeValue(user, "fax", null), PhoneNumberType.FAX_NUMBER, vcard);
		value = getAttributeValue(user, "email", null);
		if (value != null) {
			EmailAddressImpl email = new EmailAddressImpl();
			email.addType(EmailAddressType.INTERNET_EMAIL);
			email.setEmailAddress(value);
			email.setPreferred(true);
			vcard.addEmailAddress(email);
		}

		value = getAttributeValue(user, "birthdate", null);
		if (value != null) {
			Calendar birthday = Calendar.getInstance();
			birthday.setTime(Utils.parseDate(value));
			vcard.setBirthday(birthday);
		}
		
		vcard.setOutlookCompatibilityMode(true);
		
		return info.ineighborhood.cardme.io.VCardWriter.toVCardString(vcard);
	}
	
	/**
	 * Returns the attribute value.
	 * @param resource		The resource
	 * @param name			The name of the attribute
	 * @param defaultValue	The default value
	 * @return				The value of the attribute
	 */
	private static String getAttributeValue(Resource resource, String name, 
			String defaultValue) {
		ResourceAttribute attribute = resource.getResourceAttribute(name);
		if (attribute != null) {
			String value = (String) attribute.getValue();
			if (value != null) {
				return value;
			}
		}
		return defaultValue;
	}

	/**
	 * Adds a phone number to the vCard
	 * @param number	The number
	 * @param type		The type of phone number
	 * @param vcard		The vcard object
	 */
	private static void addPhone(String number, Type type, VCard vcard) {
		if (number != null) {
			PhoneNumberImpl mobile = new PhoneNumberImpl();
			mobile.addType(type);
			mobile.setLocalNumber(number);
			vcard.addPhoneNumber(mobile);
		}
	}
}
