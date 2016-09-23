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
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.13  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.12  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 *
 *
 */
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;


/**
 * @author Prashant Shah - NCICB/SAIC
 *
 *
 *
 */
public class ContrastAgentCriteria extends SingleValuePersistentCriteria
    implements Serializable {
    public final static String ENHANCED = "Enhanced";
    public final static String UNENHANCED = "Unenhanced";
    public final static String EITHER_EN_OR_UN = "Either Enhanced or Unenhanced";
    private String contrastAgentValue;

    /**
     */
    public ContrastAgentCriteria() {
        super();
    }

    /**
     * @param contrastAgentValue
     */
    public ContrastAgentCriteria(String contrastAgentValue) {
        super();
        this.contrastAgentValue = contrastAgentValue;
    }

    /**
     * @return Returns the contrastAgentValue.
     */
    public String getContrastAgentValue() {
        return contrastAgentValue;
    }

    /**
     * @param contrastAgentValue The contrastAgentValue to set.
     */
    public void setContrastAgentValue(String contrastAgentValue) {
        if (contrastAgentValue != null) {
            if (contrastAgentValue.equals(ContrastAgentCriteria.ENHANCED)) {
                this.contrastAgentValue = ContrastAgentCriteria.ENHANCED;
            } else if (contrastAgentValue.equals(
                        ContrastAgentCriteria.UNENHANCED)) {
                this.contrastAgentValue = ContrastAgentCriteria.UNENHANCED;
            } else {
                this.contrastAgentValue = ContrastAgentCriteria.EITHER_EN_OR_UN;
            }
        }
    }

    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
        setContrastAgentValue(attr.getAttributeValue());
    }

    public String getSingleValue() {
        return contrastAgentValue;
    }


    public static final class EnhancedContrast extends ContrastAgentCriteria {
        public EnhancedContrast() {
            super(ENHANCED);
        }
    }

    public static final class UnEnhancedContrast extends ContrastAgentCriteria {
        public UnEnhancedContrast() {
            super(UNENHANCED);
        }
    }

    public static final class EitherEnhancedOrUnContrast
        extends ContrastAgentCriteria {
        public EitherEnhancedOrUnContrast() {
            super(EITHER_EN_OR_UN);
        }
    }
}
