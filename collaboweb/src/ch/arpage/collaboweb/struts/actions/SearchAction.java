/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.IndexerManager;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.SearchForm;

/**
 * Performs a search inside the repository.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class SearchAction extends AbstractSecureAction {
	
	private IndexerManager indexerManager;
	
	private ResourceTypeManager resourceTypeManager;
	
	/**
	 * Set the indexerManager.
	 * @param indexerManager the indexerManager to set
	 */
	public void setIndexerManager(IndexerManager indexerManager) {
		this.indexerManager = indexerManager;
	}
	
	/**
	 * Set the resourceTypeManager.
	 * @param resourceTypeManager the resourceTypeManager to set
	 */
	public void setResourceTypeManager(ResourceTypeManager resourceTypeManager) {
		this.resourceTypeManager = resourceTypeManager;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		SearchForm searchForm = (SearchForm) form;
		
		if (searchForm.isSubmitted()) {
			try {
				request.setAttribute("list",
						indexerManager.search(searchForm.getSearchQuery(), user));
			} catch (Exception e) {
				addGlobalError(request, e);
			}
		}
		
		request.setAttribute("resourceTypes", 
				resourceTypeManager.getList(user));
		
		return mapping.findForward(Constants.SUCCESS_KEY);
	}
}
