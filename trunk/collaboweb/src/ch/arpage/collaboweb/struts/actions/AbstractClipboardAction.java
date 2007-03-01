/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Superclass of all struts actions which accesses the clipboard.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractClipboardAction extends AbstractResourceAction {

	/**
	 * Returns the clipboard (in case the clipboard is created)
	 * @param request	The request
	 * @return			The clipboard
	 */
	@SuppressWarnings("unchecked")
	protected Set<Long> getSessionClipboard(HttpServletRequest request, 
			boolean create) {
		Set<Long> clipboard = (Set<Long>) WebUtils.getSessionAttribute(
				request, Constants.CLIPBOARD);
		if (clipboard == null && create) {
			clipboard = new HashSet<Long>();
			WebUtils.setSessionAttribute(request, Constants.CLIPBOARD, 
					clipboard);
		}
		return clipboard;
	}
	
	/**
	 * Empties the clipboard
	 * @param request	The request
	 */
	protected void emptyClipboard(HttpServletRequest request) {
		WebUtils.setSessionAttribute(request, Constants.CLIPBOARD, null);
	}
}
