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
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.beans.savedquery;

import gov.nih.nci.nbia.dto.SavedQueryDTO;

import java.util.Comparator;
import java.util.Date;


public class SavedQueryComparator implements Comparator<SavedQueryDTO> {
    public int compare(SavedQueryDTO o1, SavedQueryDTO o2) {
        SavedQueryDTO one = (SavedQueryDTO) o1;
        SavedQueryDTO two = (SavedQueryDTO) o2;

        if ((one == null) || (two == null)) {
            return 0;
        }

        Date oneExecTime = one.getExecutionTime();
        Date twoExecTime = two.getExecutionTime();

        if ((oneExecTime == null) && (twoExecTime == null)) {
            return 0;
        }

        if (oneExecTime == null) {
            return 1;
        }

        if (twoExecTime == null) {
            return -1;
        }

        return (twoExecTime).compareTo(oneExecTime);
    }
}
