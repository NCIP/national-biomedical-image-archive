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
* Revision 1.1  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.7  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.6  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.querystorage;

/**
 * Lightweight class used by Criteria subclasses to set
 * values for storage in the database
 *
 */
public class QueryAttributeWrapper {
    // Used to store the order in which attributes are stored
    private String subAttributeName;

    // The value of the attribute
    private String attributeValue;

    // Fully qualified class name of the criteria subclass to store
    private String criteriaClassName;

    /**
     * Default constructor
     *
     */
    public QueryAttributeWrapper() {
    }

    /**
     * Constructor that provides an easy way to create this class by passing parameters
     *
     * @param criteriaClassName - the class name of the criteria
     * @param attributeValue - the value of the attribute
     * @param subAttributeName - used to store the order in which attributes are stored
     */
    public QueryAttributeWrapper(String criteriaClassName,
        String attributeValue, String subAttributeName) {
        this.attributeValue = attributeValue;
        this.criteriaClassName = criteriaClassName;
        this.subAttributeName = subAttributeName;
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

    /**
     *
     */
    public String getCriteriaClassName() {
        return criteriaClassName;
    }

    /**
     *
     * @param criteriaClassName
     */
    public void setCriteriaClassName(String criteriaClassName) {
        this.criteriaClassName = criteriaClassName;
    }
}
