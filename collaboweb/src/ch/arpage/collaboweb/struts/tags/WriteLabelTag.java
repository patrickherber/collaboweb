/**
 * collaboweb
 * Feb 12, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.bean.WriteTag;

import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Tag used to write the label of an object in the current language
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class WriteLabelTag extends WriteTag {
	
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String language;
	
	/**
	 * Set the language.
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * Returns the language.
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	// --------------------------------------------------------- Public Methods

    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Look up the requested bean (if necessary)
        if (ignore) {
            if (TagUtils.getInstance().lookup(pageContext, name, scope) == null) {
                return (SKIP_BODY); // Nothing to output
            }
        }

        // Look up the requested property value
        Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);

        if (value == null) {
            return (SKIP_BODY); // Nothing to output
        }

        if (value instanceof Map) {
        	Map map = (Map) value;
        	value = null;
        	if (language == null) {
        		Object sessionLanguage = TagUtils.getInstance().lookup(pageContext, 
        				Constants.LANGUAGE, "session");
        		if (sessionLanguage instanceof String) {
        			language = (String) sessionLanguage;
        		}
        	}
        	value = map.get(language);
        	if (value == null) {
        		value = map.get(Constants.DEFAULT_LANGUAGE);
        	}
        	if (value == null) {
        		value = "";
        	}
        }
        // Convert value to the String with some formatting
        String output = formatValue(value);

        // Print this property value to our output writer, suitably filtered
        if (filter) {
            TagUtils.getInstance().write(pageContext, TagUtils.getInstance().filter(output));
        } else {
            TagUtils.getInstance().write(pageContext, output);
        }

        // Continue processing this page
        return (SKIP_BODY);

    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        language = null;

    }
}
