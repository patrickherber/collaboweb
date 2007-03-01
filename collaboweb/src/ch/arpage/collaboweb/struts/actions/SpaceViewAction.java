/**
 * collaboweb
 * Jan 27, 2007
 */
package ch.arpage.collaboweb.struts.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.ResourceType;
import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.services.ResourceTypeManager;
import ch.arpage.collaboweb.struts.common.Constants;

/**
 * Navigates inside the hierarchy of the repository.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class SpaceViewAction extends AbstractResourceAction {
	
	private ResourceTypeManager resourceTypeManager;
	
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
		
		long resourceId = 
			ServletRequestUtils.getLongParameter(request, "resourceId", 0);
		try {
			if (resourceId == 0) {
				resourceId = user.getCommunity().getResourceRootId();
			}
			Resource resource = resourceManager.get(resourceId, user);
			request.setAttribute("resource", resource);
			setSupportedChildren(resource, user, request);
			request.setAttribute("list", 
					resourceManager.getList(resourceId, user));
			if (WebUtils.getSessionAttribute(request, Constants.CLIPBOARD) 
					!= null) {
				request.setAttribute("relationshipTypes", 
						resourceTypeManager.getRelationshipTypes(user));
			}
		} catch (DataRetrievalFailureException e) {
			addGlobalError(request, "errors.spaceNotFound", resourceId);
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		
		return mapping.findForward(Constants.SUCCESS_KEY);
	}


	/**
	 * Sets in the request the list of supported children of the given resource
	 * @param resource	The current resource
	 * @param user		The current user
	 * @param request	The request
	 */
	private void setSupportedChildren(Resource resource, User user,
			HttpServletRequest request) {
		List<Integer> ids = 
			resource.getResourceType().getSupportedChildrenIds();
		if (ids != null && ids.size() > 0) {
			List<ResourceType> children = 
				new ArrayList<ResourceType>(ids.size());
			List<ResourceType> resourceTypes = 
				resourceTypeManager.getList(user);
			for (ResourceType resourceType : resourceTypes) {
				if (ids.contains(resourceType.getTypeId())) {
					children.add(resourceType);
				}
			}
			request.setAttribute("supportedChildren", children);
		}	
	}
}
