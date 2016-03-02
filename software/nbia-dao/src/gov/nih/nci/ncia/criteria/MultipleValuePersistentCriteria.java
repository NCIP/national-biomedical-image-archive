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
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Persistent criteria that can be set to have a list of values
 *
 * @author dietrichj
 *
 */
public abstract class MultipleValuePersistentCriteria extends PersistentCriteria {
    /**
     * Returns true if no values have been set
     */
    public boolean isEmpty() {
        return (getMultipleValues() == null) || getMultipleValues().isEmpty();
    }

    /**
     * Converts the criteria's values into QueryAttributeWrappers
     */
    protected List<QueryAttributeWrapper> getQueryAttributes(
        Collection<String> values) {
        List<QueryAttributeWrapper> attrList = new ArrayList<QueryAttributeWrapper>();

        int subAttrName = 1;

        // Create a wrapper for each of the criteria's values
        for (String value : values) {
            QueryAttributeWrapper attr = new QueryAttributeWrapper();
            attr.setCriteriaClassName(getClass().getName());
            attr.setSubAttributeName(String.valueOf(subAttrName++));
            attr.setAttributeValue(value);
            attrList.add(attr);
        }

        return attrList;
    }

    /**
     * Populates the criteria from a QueryAttribute
     */
    public List<QueryAttributeWrapper> getQueryAttributes() {
        return getQueryAttributes(getMultipleValues());
    }

    /**
     * Returns the values of this criteria
     */
    protected abstract Collection<String> getMultipleValues();

    /**
     * Returns a string to display to the user representing
     * this criteria's value
     */
    public String getDisplayValue() {
        return Util.getCommaSeparatedList(getMultipleValues());
    }
}
