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
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import java.util.Comparator;

import javax.faces.model.SelectItem;


public class SelectItemComparator implements Comparator<SelectItem> {
    public int compare(SelectItem o1, SelectItem o2) {
        SelectItem one = (SelectItem) o1;
        SelectItem two = (SelectItem) o2;

        if ((one == null) || (two == null)) {
            return 0;
        }

        return one.getValue().toString().compareTo(two.getValue().toString());
    }
}
