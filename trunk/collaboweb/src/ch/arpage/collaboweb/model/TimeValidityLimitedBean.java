/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.util.Assert;

/**
 * This abstract class defines methods for objects that have a validity limited
 * in the time (from / to date).
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class TimeValidityLimitedBean extends AbstractBean {

	private Timestamp validFrom;
	private Timestamp validTo;
	
	/**
	 * Returns the validFrom.
	 * @return the validFrom
	 */
	public Timestamp getValidFrom() {
		return validFrom;
	}
	
	/**
	 * Set the validFrom.
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}
	
	/**
	 * Returns the validTo.
	 * @return the validTo
	 */
	public Timestamp getValidTo() {
		return validTo;
	}
	
	/**
	 * Set the validTo.
	 * @param validTo the validTo to set
	 */
	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
	
	public boolean isValid() {
		return isValid(new Date());
	}
	
	public boolean isValid(Date date) {
		Assert.notNull(date);
		long time = date.getTime();
		return ((validFrom == null || validFrom.getTime() <= time) && 
				(validTo == null || validTo.getTime() >= time));
	}
}
