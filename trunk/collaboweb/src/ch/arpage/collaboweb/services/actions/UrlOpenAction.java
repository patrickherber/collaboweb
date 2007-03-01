/**
 * collaboweb
 * Feb 5, 2007
 */
package ch.arpage.collaboweb.services.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceAttribute;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceManager;

/**
 * Open an URL
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class UrlOpenAction implements Action {
	
	private ResourceManager resourceManager;
	
	/**
	 * Set the resourceManager.
	 * @param resourceManager the resourceManager to set
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.services.actions.Action#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, ch.arpage.collaboweb.model.User, java.lang.String)
	 */
	public String execute(HttpServletRequest request, 
			HttpServletResponse response, User user, String parameter) 
			throws Exception {

		long resourceId = 
			ServletRequestUtils.getLongParameter(request, "resourceId", 0L);
		
		Resource resource = resourceManager.get(resourceId, user);
		
		ResourceAttribute ra = resource.getResourceAttribute(9);
		if (ra != null) {
			String url = (String) ra.getValue();
			if (StringUtils.hasText(url)) {
				if (url.indexOf("://") != -1) {
					response.sendRedirect(url);
				} else {
					response.sendRedirect("http://" + url);
				}
			}
		}
		return null;
	}

}
