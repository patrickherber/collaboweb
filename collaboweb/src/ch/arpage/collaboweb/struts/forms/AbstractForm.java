/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.forms;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Superclass of all application struts ActionForm classes.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractForm extends ActionForm {

	private boolean submitted;
	
	/**
	 * Returns the submitted.
	 * @return the submitted
	 */
	public boolean isSubmitted() {
		return submitted;
	}
	
	/**
	 * Set the submitted.
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.submitted = false;
	}
	
	/** 
	 * Returns a string representation of this form
	 * @return A string representation of this form
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName()).append(":");
		Method[] methods = getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String name = methods[i].getName();
			if ((name.startsWith("get") || name.startsWith("is")) && 
					methods[i].getParameterTypes().length == 0) {
				if (name.startsWith("get")) {
					sb.append(name.substring(3));
				} else {
					sb.append(name.substring(2));					
				}
				sb.append("=");
				try {
					sb.append(methods[i].invoke(this, (Object[]) null));
				} catch (Exception e) {
					sb.append("null");
				}
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
