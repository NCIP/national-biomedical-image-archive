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
* Revision 1.7  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.6  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import java.util.Date;


public class CurationStatusDateCriteria extends TransientCriteria {
    
    private Date curationStatusDate;

    public boolean isEmpty() {
        return (getCurationStatusDate() == null);
    }

    public Date getCurationStatusDate() {
        return curationStatusDate;
    }

    public void setCurationStatusDate(Date curationStatusDate) {
        this.curationStatusDate = curationStatusDate;
    }
}
