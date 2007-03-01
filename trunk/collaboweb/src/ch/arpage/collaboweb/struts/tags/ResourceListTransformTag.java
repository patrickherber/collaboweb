/**
 * collaboweb
 * Feb 21, 2007
 */
package ch.arpage.collaboweb.struts.tags;

import java.util.List;

import ch.arpage.collaboweb.model.Resource;

/**
 * Tag used to transform a list of resources using the stylesheet 
 * for the view type gived as parameter.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceListTransformTag extends AbstractTransformTag {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Resource> list; // attribute
	/**
	 * Creates a ResourceTransformTag
	 */
	public ResourceListTransformTag() {
		super();
		init();
	}

	/**
	 * Initialize the tag
	 */
	protected void init() {
		list = null;
		super.init();
	}
	
	/**
	 * Set the list.
	 * @param list the list to set
	 */
	@SuppressWarnings("unchecked")
	public void setList(Object list) {
		this.list = (List<Resource>) list;
	}
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.tags.AbstractTransformTag#getXml()
	 */
	@Override
	protected String getXml() throws Exception {
		StringBuffer sb = new StringBuffer();
		if (list != null) {
			sb.append("<list>");
			for (Resource r : list) {
				sb.append(r.getXml());
			}
			sb.append("</list>");
		}
		return sb.toString();
	}
}
