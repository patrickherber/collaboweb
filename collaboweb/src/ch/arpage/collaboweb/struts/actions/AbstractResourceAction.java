/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import ch.arpage.collaboweb.services.ResourceManager;

/**
 * Superclass of all struts actions which need to access the resource
 * manager service.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public abstract class AbstractResourceAction extends AbstractSecureAction {
	
	protected ResourceManager resourceManager;
	
	/**
	 * Set the resourceManager.
	 * @param resourceManager the resourceManager to set
	 */
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
}
