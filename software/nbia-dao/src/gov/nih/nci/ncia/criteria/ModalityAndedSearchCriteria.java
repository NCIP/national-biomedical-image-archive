/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mccrimmons
 * Date: Nov 2, 2007
 * Time: 10:41:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModalityAndedSearchCriteria extends SingleValuePersistentCriteria
    implements Serializable {

    private String modalityAndedSearchValue;

    public String getModalityAndedSearchValue() {
        return modalityAndedSearchValue;
    }

    public void setModalityAndedSearchValue(String modalityAndedSearchValue) {
        this.modalityAndedSearchValue = modalityAndedSearchValue;
    }

    public String getSingleValue() {
        return getModalityAndedSearchValue();
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attribute) {
        setModalityAndedSearchValue(attribute.getAttributeValue());
    }

    /**
     * Returns a name of this type of criteria to display
     * to the user
     */
    public String getDisplayName() {
        return "Return cases that include";
    }

    /**
     * Returns a string to display to the user representing
     * this criteria's value
     */
    public String getDisplayValue() {
        return "all".equals(modalityAndedSearchValue) ? "all of these modalites" : "any of these modalities";
    }
}
