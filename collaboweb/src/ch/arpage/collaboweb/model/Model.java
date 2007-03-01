/**
 * collaboweb
 * Feb 15, 2007
 */
package ch.arpage.collaboweb.model;

/**
 * Model constants
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface Model {

	/** No access right */
	int RIGHT_NONE = 0;
	/** Read assess right */
	int RIGHT_READ = 1;
	/** Contribute access right (can add child-object to the current object) */
	int RIGHT_CONTRIBUTE = 2;
	/** Modify access right (can modify the current object)*/
	int RIGHT_MODIFY = 3;
	/** Admin access right (can manage and also delete the current object) */
	int RIGHT_ADMIN = 4;
	
	/** Data type string */
	int DATA_TYPE_STRING = 1;
	/** Data type integer */
	int DATA_TYPE_INTEGER = 2;
	/** Data type double */
	int DATA_TYPE_DOUBLE = 3;
	/** Data type date */
	int DATA_TYPE_DATE = 4;
	/** Data type binary (file/image) */
	int DATA_TYPE_BINARY = 5;
}
