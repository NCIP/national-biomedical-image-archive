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
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.5  2007/01/04 03:14:49  dietrich
* Added code to protect against null values for getDisplayName()
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.criteria.Criteria;

import java.util.Comparator;


public class CriteriaComparator implements Comparator<Criteria> {
    public int compare(Criteria o1, Criteria o2) {
        if ((o1 == null) || (o2 == null)) {
            return 0;
        }

        if( (o1.getDisplayName() == null) &&
            (o2.getDisplayName() == null)) {
            return 0;
        }
        
        if(o1.getDisplayName() == null) {
            return -1;
        }
        
        if(o1.getDisplayName() == null) {
            return 1;
        }        
        
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}
