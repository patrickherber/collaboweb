/**
 * collaboweb
 * Feb 23, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.View;

/**
 * Tag used to evaluate of the given resource has actions for the given view
 * type.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class HasActionsForViewTag extends TagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Resource resource;
	private int viewTypeId;
	
	/**
	 * Set the resource.
	 * @param resource the resource to set
	 */
	public void setResource(Object resource) {
		this.resource = (Resource) resource;
	}
	
	/**
	 * Set the viewTypeId.
	 * @param viewTypeId the viewTypeId to set
	 */
	public void setViewTypeId(int viewTypeId) {
		this.viewTypeId = viewTypeId;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
        if (hasActions()) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
	}

	/**
	 * Returns <code>true</code> if the resource type has actions for the
	 * given view type, <code>false</code> otherwise.
	 * @return	<code>true</code> if the resource type has actions for the
	 * given view type, <code>false</code> otherwise
	 */
	private boolean hasActions() {
		if (resource != null) {
			View view = resource.getResourceType().getView(viewTypeId);
			if (view != null) {
				List<Action> list = view.getActions();
				return (list != null && list.size() > 0);
			}
		}
		return false;
	}
}
