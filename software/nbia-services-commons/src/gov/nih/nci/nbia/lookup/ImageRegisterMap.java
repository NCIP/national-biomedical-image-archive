/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import java.util.HashMap;
import java.util.Map;

/**
 * This object maps magical identifiers to the path to a thumb nail image file.
 * 
 * <p>This is information stored in a ThumbnailImageDTO as populated by
 * ResultManager.
 * 
 * <p>The thumb nail servlet later takes the identifier from the client and then
 * uses it to reference here to get the path to send the bytes of the 
 * thumb nail down to the client
 * 
 * <p>This object is implicitly a singleton for the entire application since it
 * is an attribute of ApplicationFactory.  Therefore.... this object leaks memory
 * a little bit given that it never releases an <integer, filePath> tuple.  This
 * map should probably be per session instead?
 *
 */
public class ImageRegisterMap {
    private Map<Integer, String> registeredFilePaths = null;
    
    /**
     * Keeps track of the next registered file path ID.
     */
    private int nextRegisteredFilePathId = 1;
    
    public ImageRegisterMap()  {
        registeredFilePaths = new HashMap<Integer, String>();
    }
    
    
    /**
     * Register a file path and get an ID for it
     *
     * @param filePath - path to the file being registered
     * @return the registered ID of the file path
     */
    public int registerFilePath(String filePath) {
        // Get the ID to use
        int registrationId = getNextFilePathId();
        // Add to the HashMap
        registeredFilePaths.put(registrationId, filePath);

        return registrationId;
    }
    
    /**
     * Look up the path based on a registered file path ID
     *
     * @param registeredId
     */
    public String lookupRegisteredPath(int registeredId) {
        return registeredFilePaths.get(registeredId);
    }   
    
    /**
     * Increments the registered file path ID
     */
    private synchronized int getNextFilePathId() {
        return ++nextRegisteredFilePathId;
    }    
}
