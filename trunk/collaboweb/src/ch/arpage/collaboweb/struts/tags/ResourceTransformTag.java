/**
 * collaboweb
 * Feb 18, 2007
 */
package ch.arpage.collaboweb.struts.tags;


/**
 * Tag used to transform a resource using the stylesheet for the view type 
 * gived as parameter.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ResourceTransformTag extends AbstractTransformTag {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see ch.arpage.collaboweb.struts.tags.AbstractTransformTag#getXml()
	 */
	@Override
	protected String getXml() throws Exception {
		return resource.getXml();
	}
}
