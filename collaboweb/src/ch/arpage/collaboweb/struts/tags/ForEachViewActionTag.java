/**
 * collaboweb
 * Feb 23, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;

import ch.arpage.collaboweb.model.Action;
import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.View;

/**
 * Iteration Tag over the list of actions, visible for a resource type inside
 * a given view type.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ForEachViewActionTag  extends BodyTagSupport {

    // ----------------------------------------------------- Instance Variables

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Iterator of the elements of this collection, while we are actually
     * running.
     */
    protected Iterator iterator = null;

    /**
     * The number of elements we have already rendered.
     */
    protected int lengthCount = 0;

    /**
     * Has this tag instance been started?
     */
    protected boolean started = false;

    // ------------------------------------------------------------- Properties


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

    /**
     * The name of the scripting variable to be exposed.
     */
    protected String id = null;

    public String getId() {
        return (this.id);
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>Return the zero-relative index of the current iteration through the
     * loop.  If you specify an <code>offset</code>, the first iteration
     * through the loop will have that value; otherwise, the first iteration
     * will return zero.</p>
     *
     * <p>This property is read-only, and gives nested custom tags access to
     * this information.  Therefore, it is <strong>only</strong> valid in
     * between calls to <code>doStartTag()</code> and <code>doEndTag()</code>.
     * </p>
     */
    public int getIndex() {
        if (started)
            return (lengthCount - 1);
        else
            return (0);
    }

    /**
     * The name of the scripting variable to be exposed as the current index.
     */
    protected String indexId = null;

    public String getIndexId() {
        return (this.indexId);
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Construct an iterator for the specified collection, and begin
     * looping through the body once per element.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

    	if (resource != null && viewTypeId != 0) {
    		View view = resource.getResourceType().getView(viewTypeId);
			if (view != null) {
				List<Action> list = view.getActions();
				if (list != null && list.size() > 0) {
					iterator = list.iterator();
				}
			}
    	}

        // Store the first value and evaluate, or skip the body if none
        if (iterator.hasNext()) {
            Object element = iterator.next();
            if (element == null) {
                pageContext.removeAttribute(id);
            } else {
                pageContext.setAttribute(id, element);
            }
            lengthCount++;
            started = true;
            if (indexId != null) {
                pageContext.setAttribute(indexId, new Integer(getIndex()));
            }
            return (EVAL_BODY_BUFFERED);
        } else {
            return (SKIP_BODY);
        }

    }

    /**
     * Make the next collection element available and loop, or
     * finish the iterations if there are no more elements.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        // Render the output from this iteration to the output stream
        if (bodyContent != null) {
            TagUtils.getInstance().writePrevious(pageContext, bodyContent.getString());
            bodyContent.clearBody();
        }

        if (iterator.hasNext()) {
            Object element = iterator.next();
            if (element == null) {
                pageContext.removeAttribute(id);
            } else {
                pageContext.setAttribute(id, element);
            }
            lengthCount++;
            if (indexId != null) {
                pageContext.setAttribute(indexId, new Integer(getIndex()));
            }
            return (EVAL_BODY_BUFFERED);
        } else {
            return (SKIP_BODY);
        }

    }

    /**
     * Clean up after processing this enumeration.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Clean up our started state
        started = false;
        iterator = null;

        // Continue processing this page
        return (EVAL_PAGE);

    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        resource = null;
        viewTypeId = 0;
        iterator = null;
        lengthCount = 0;
        id = null;
        started = false;
    }
}
