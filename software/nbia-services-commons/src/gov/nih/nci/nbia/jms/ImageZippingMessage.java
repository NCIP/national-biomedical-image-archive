/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.jms;

import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class ImageZippingMessage implements Serializable {	
	private static final long serialVersionUID = -5320008016599256927L;
    
    private String emailAddress;
    private String zipFilename;
    private String supportPhoneNumber;
    private String nodeName;
    private String userName;

    // Map of series to be zipped
    // Key is the PK ID of the series
    private Map<String, SeriesSearchResult> items = new HashMap<String, SeriesSearchResult>();
    private boolean includeAnnotation;
    
    public ImageZippingMessage() {
    }
    /**
     * @return Returns the nodeName.
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName The nodeName to set.
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getEmailAddress() {    	
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Map<String, SeriesSearchResult> getItems() {
        return this.items;
    }

    public void setItems(Map<String, SeriesSearchResult> items) {
    	Set<String> keys = items.keySet();
        this.items = new HashMap<String, SeriesSearchResult>();
        for(String key : keys) {
        	SeriesSearchResult vals = (SeriesSearchResult)items.get(key);
            this.items.put(key, vals);
        } 
    }

    public String getZipFilename() {
        return zipFilename;
    }

    public void setZipFilename(String zipFilename) {
        this.zipFilename = zipFilename;
    }


    public String getSupportPhoneNumber() {
        return supportPhoneNumber;
    }

    public void setSupportPhoneNumber(String supportPhoneNumber) {
        this.supportPhoneNumber = supportPhoneNumber;
    }


    /**
     * @return Returns the includeAnnotation.
     */
	public boolean isIncludeAnnotation() {
		return includeAnnotation;
	}
	/**
     * @param includeAnnotation The includeAnnotation to set.
     */
	public void setIncludeAnnotation(boolean includeAnnotation) {
		this.includeAnnotation = includeAnnotation;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
