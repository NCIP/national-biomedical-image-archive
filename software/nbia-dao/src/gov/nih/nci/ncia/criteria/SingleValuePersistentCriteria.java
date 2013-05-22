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
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.8  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import gov.nih.nci.nbia.util.QueryStorageConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * Persistent criteria that is set to a single value (as opposed
 * to a list of values)
 *
 * @author dietrichj
 *
 */
public abstract class SingleValuePersistentCriteria extends PersistentCriteria {
    /**
     * Returns the single value of this criteria
     */
    public abstract String getSingleValue();

    /**
     * Returns true if the single value is empty or null
     **/
    public boolean isEmpty() {
        return (getSingleValue() == null) || getSingleValue().equals("");
    }

    /**
     * Converts the criteria's value into QueryAttributeWrappers
     */
    public List<QueryAttributeWrapper> getQueryAttributes() {
        // Create one QueryAttributeWrapper copying values from the criteria
        QueryAttributeWrapper attr = new QueryAttributeWrapper();
        attr.setCriteriaClassName(getClass().getName());
        attr.setSubAttributeName(QueryStorageConstants.FIRST_SUB_ATTR);
        attr.setAttributeValue(getSingleValue());

        List<QueryAttributeWrapper> attrList = new ArrayList<QueryAttributeWrapper>();
        attrList.add(attr);

        return attrList;
    }

    /**
     * Returns a string to display to the user representing
     * this criteria's value
     */
    public String getDisplayValue() {
        return getSingleValue();
    }
}
