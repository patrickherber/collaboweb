/**
 * collaboweb
 * Feb 4, 2007
 */
package ch.arpage.collaboweb.model;

import java.io.InputStream;

/**
 * Binary attribute model class, holds an InputStream with the content of a 
 * bynary field.
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class BinaryAttribute {

	private String name;
	private InputStream inputStream;
	private String contentType;
	
	/**
	 * @param name
	 * @param inputStream
	 * @param contentType
	 */
	public BinaryAttribute(String name, InputStream inputStream, 
			String contentType) {
		this.name = name;
		this.inputStream = inputStream;
		this.contentType = contentType;
	}
	
	/**
	 * Returns the contentType.
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Returns the inputStream.
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
