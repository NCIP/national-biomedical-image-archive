/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.io.Serializable;

/**
 * This object corrals all the searchable terms for a given node.
 *
 * <P>This object should be considered immutable.... even though
 * it doesn't enforce that.
 *
 * <P><b>WARNING!</b> This object is serialized so if you change it, you risk
 * breaking remote search and the grid interface
 */
public class UsAvailableSearchTerms implements Serializable {


    public UsAvailableSearchTerms() {
    }

    /**
     * Gets the UsMultiModalities value for this UsAvailableSearchTerms.
     *
     * @return UsMultiModalities
     */
    public String[] getUsMultiModalities() {
        return usMultiModalities;
    }

    /**
     * Sets the usMultiModalities value for this UsAvailableSearchTerms.
     */
    public void setUsMultiModalities(String[] usMultiModalities) {
        this.usMultiModalities = usMultiModalities;
    }

    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */
    public String getUsMultiModalities(int i) {
        return this.usMultiModalities[i];
    }

    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */
    public void setUsMultiModalities(int i, String _value) {
        this.usMultiModalities[i] = _value;
    }


    ///////////////////////////////////////PRIVATE////////////////////////////////////////

    private String[] usMultiModalities;
}
