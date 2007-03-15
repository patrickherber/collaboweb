/**
 * collaboweb
 * Jan 13, 2007
 */
package ch.arpage.collaboweb.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import ch.arpage.collaboweb.struts.common.Constants;

/**
 * This abstract class defines the method used for reading and writing the
 * object's labels (internationalising)
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class LabelableBean extends AbstractBean {
	
	private Map<String, String> labels;
	
	/**
	 * Returns the labels.
	 * @return the labels
	 */
	public Map<String, String> getLabels() {
		checkLabels();
		return labels;
	}
	
	/**
	 * Set the labels.
	 * @param labels the labels to set
	 */
	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}
	
	public void setLabel(String key, String value) {
		checkLabels();
		this.labels.put(key, value);
	}

	private void checkLabels() {
		if (labels == null) {
			labels = new HashMap<String, String>();
		}
	}
	
	public String getLabel(String language) {
		checkLabels();
		String label = labels.get(language);
		if (!StringUtils.hasText(label)) {
			label = labels.get(Constants.DEFAULT_LANGUAGE);
		}
		return label;
	}
}
