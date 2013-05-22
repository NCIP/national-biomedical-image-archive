/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.6  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.5  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.querystorage;


/**
 * Abstract superclass of QueryHistoryAttribute and SavedQueryAttribute.
 *
 * This class was created because much of the behavior of those subclasses is
 * the same.
 *
 */
public abstract class QueryAttribute {
    // Primary key
    private Long id;

    // Name of the attribute is the fully qualified classname (subclass of criteria)
    private String attributeName;

    // Used to store the order of the attributes
    private String subAttributeName;

    // Value of the criteria
    private String attributeValue;

    // Determines which instance of the criteria this belongs to
    // (only relevant in situations such as CurationDataCriteria where
    //  there can more than one instance of a criteria class per query) 
    private Integer instanceNumber;

    /**
     *
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     *
     * @param attributeName
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     *
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     *
     * @param attributeValue
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    /**
     *
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     */
    public String getSubAttributeName() {
        return subAttributeName;
    }

    /**
     *
     * @param subAttributeName
     */
    public void setSubAttributeName(String subAttributeName) {
        this.subAttributeName = subAttributeName;
    }

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(Integer instanceNum) {
        this.instanceNumber = instanceNum;
    }
    
    /**
     * Converts to a QueryAttributeWrapper
     * 
     * @return QueryAttributeWrapper
     */
    public QueryAttributeWrapper getQueryAttributeWrapper() {
        return new QueryAttributeWrapper(this.getAttributeName(), this.getAttributeValue(), this.getSubAttributeName());
        
    }
    
}
