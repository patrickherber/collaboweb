/**
 * collaboweb
 * Created on 17.02.2007
 */
package ch.arpage.collaboweb.services.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceManager;

/**
 * Export the user data als VCF File (VCARD FILE)
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class VCardExportAction implements Action {
	
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
		if (resourceId != 0L) {
			Resource resource = resourceManager.get(resourceId, user);
			response.addHeader("Content-Disposition", 
					"attachment; filename=\""
					 + resource.getName()
					 + ".vcf\"");
			response.setContentType(VCardWriter.CONTENT_TYPE);
			PrintWriter writer = response.getWriter();
			writer.write(VCardWriter.getVCard(resource));
			writer.close();
		}
		return null;
	}
}
