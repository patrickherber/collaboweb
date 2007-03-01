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
import org.springframework.web.bind.ServletRequestUtils;

import ch.arpage.collaboweb.model.User;
import ch.arpage.collaboweb.struts.common.Constants;
import ch.arpage.collaboweb.struts.forms.ResourceForm;

/**
 * Struts action used to save a resource.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceAction extends AbstractResourceAction {

	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.actions.AbstractSecureAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, ch.arpage.collaboweb.model.User, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, 
			User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		ResourceForm resourceForm = (ResourceForm) form;
		
		try {
			if (resourceForm.isSubmitted()) {
				resourceForm.updateAttributeValues();
				resourceManager.save(resourceForm.getResource(), user);
				return redirect(request, response, mapping, Constants.SUCCESS_KEY, 
						"?resourceId=" + resourceForm.getResourceId());
			} else {
				if (resourceForm.getResourceId() != 0L) {
					resourceForm = new ResourceForm(
							resourceManager.get(resourceForm.getResourceId(), 
									user));
				} else {
					long parentId = ServletRequestUtils.getLongParameter(
							request, "parentId", 0);
					int typeId = ServletRequestUtils.getIntParameter(
							request, "typeId", 0);
					resourceForm = new ResourceForm(
							resourceManager.create(parentId, typeId, user));					
				}
			}
		} catch (Exception e) {
			addGlobalError(request, e);
		}
		
		request.setAttribute("resourceForm", resourceForm);
		
		return mapping.getInputForward();
	}
}
