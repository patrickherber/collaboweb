/**
 * collaboweb
 * Created on 17.02.2007
 */
package ch.arpage.collaboweb.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;

/**
 * Parser class used to parse a MS Power-Point file into a string.
 * 
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class MSPowerPointParser implements POIFSReaderListener {

    private ByteArrayOutputStream writer;

    /**
     * Returns the content of the given MS Power-Point stream
     * @param stream		The MS Power-Point stream 
     * @return				The content of the file as text
     * @throws Exception
     */
	public String getContents(InputStream stream) throws Exception {
		try {
            POIFSReader reader = new POIFSReader();
            writer = new ByteArrayOutputStream();
            reader.registerListener(this);
            reader.read(stream);
            return writer.toString();
        } catch (Exception e) {
            throw e;
        }
	}
	
    /* (non-Javadoc)
     * @see org.apache.poi.poifs.eventfilesystem.POIFSReaderListener#processPOIFSReaderEvent(org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent)
     */
    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
    	try {
			if(!"PowerPoint Document".equalsIgnoreCase(event.getName())) {
				return;
			}
			DocumentInputStream input = event.getStream();
			byte[] buffer = new byte[input.available()];
			input.read(buffer, 0, input.available());
			for (int i = 0; i < buffer.length - 20; i++) {
				long type = LittleEndian.getUShort(buffer,i+2);
				long size = LittleEndian.getUInt(buffer,i+4);
				if (type == 4008) {
					writer.write(buffer, i + 4 + 1, (int) size);
					i = i + 4 + 1 + (int) size - 1;
				}
			}
        } catch (Exception e) { 
        	/* do nothing */
        }
    }
}