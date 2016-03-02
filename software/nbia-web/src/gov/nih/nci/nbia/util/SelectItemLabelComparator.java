/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.Comparator;

import javax.faces.model.SelectItem;

public class SelectItemLabelComparator implements Comparator<SelectItem> {
    public int compare(SelectItem o1, SelectItem o2) {
    	SelectItem one = (SelectItem) o1;
    	SelectItem two = (SelectItem) o2;
        if ((one == null) || (two == null)) {
            return 0;
        }
        return one.getLabel().compareTo(two.getLabel());
    }
}
