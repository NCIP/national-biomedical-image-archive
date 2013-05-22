/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import gov.nih.nci.nbia.querystorage.QueryAttribute;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;


/**
 * Domain class to represent the criteria stored for a saved query record
 *
 * @author NCIA Team
 */
public class SavedQueryAttribute extends QueryAttribute implements Serializable {
    private static final long serialVersionUID = 2581622057143043273L;

    //  Pointer to this object's parent 
    private SavedQuery parentQuery;

    public SavedQueryAttribute() {
    }

    /**
     * Creates an object based on a QueryAttributeWrapper
     *
     * @param wrapper
     * @param parent
     */
    public SavedQueryAttribute(QueryAttributeWrapper wrapper,
        SavedQuery parent, int instanceNumber) {
        setAttributeName(wrapper.getCriteriaClassName());
        setAttributeValue(wrapper.getAttributeValue());
        setSubAttributeName(wrapper.getSubAttributeName());
        setParentQuery(parent);
        setInstanceNumber(instanceNumber);
    }

    public SavedQuery getParentQuery() {
        return parentQuery;
    }

    public void setParentQuery(SavedQuery parentQuery) {
        this.parentQuery = parentQuery;
    }
}
