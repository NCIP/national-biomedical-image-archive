/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

/**
 * This represents a criteria in the DICOM Query.  It uses the class
 * name of the criteria object to communicate the criteria.  A collection
 * of these are sent across the grid (to search for patients).
 * 
 * <P>This object should be considered immutable.... even though
 * it doesn't enforce that.
 * 
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public class SearchCriteriaDTO  {


    public SearchCriteriaDTO() {
    }

    /**
     * Get the class name of the criteria
     */
    public String getType() {
        return type;
    }
    
    
    /**
     * Set the class name of the criteria
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
    /**
     * Get the value for the criteria.  For example, for a modality
     * criteria, this value might be CT.
     */    
    public String getValue() {
        return value;
    }
    
    
    /**
     * Set the value for the criteria.  For example, for a modality
     * criteria, this value might be CT.
     */      
    public void setValue(String value) {
        this.value = value;
    }

    
    /**
     * I'm a little hazy on this.... but take a look at date range
     * criteria (1 and 2) for an example.
     */
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
    

    ///////////////////////////////////PRIVATE////////////////////////////////
    private String type;
    private String value;
    private String subType;    
}
