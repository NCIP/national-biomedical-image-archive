/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: QueryHistoryAttribute.java 6028 2008-08-13 19:42:42Z panq $
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:13  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/**
 *
 * copyright
 */
package gov.nih.nci.nbia.internaldomain;

import gov.nih.nci.nbia.querystorage.QueryAttribute;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;


/**
 * Domain class to represent the criteria stored for a query history record
 * @author NCIA Team
 */
public class QueryHistoryAttribute extends QueryAttribute
    implements Serializable {
    private static final long serialVersionUID = 2581622057143043273L;

    //  Pointer to this object's parent
    private QueryHistory parentQuery;

    public QueryHistoryAttribute() {
    }

    /**
     * Creates an object based on a QueryAttributeWrapper
     *
     * @param wrapper
     * @param parent
     */
    public QueryHistoryAttribute(QueryAttributeWrapper wrapper,
        QueryHistory parent, Integer instanceNumber) {
        // Transfer the data from the wrapper to this object.
        setAttributeName(wrapper.getCriteriaClassName());
        setAttributeValue(wrapper.getAttributeValue());
        setSubAttributeName(wrapper.getSubAttributeName());
        setParentQuery(parent);
        setInstanceNumber(instanceNumber);
    }

    /**
     * 
     */
    public QueryHistory getParentQuery() {
        return parentQuery;
    }

    /**
     *
     * @param parentQuery
     */
    public void setParentQuery(QueryHistory parentQuery) {
        this.parentQuery = parentQuery;
    }
}
