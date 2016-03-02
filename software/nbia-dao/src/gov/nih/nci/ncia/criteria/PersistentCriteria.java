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
* Revision 1.3  2007/08/15 16:50:00  bauerd
* *** empty log message ***
*
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.9  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.8  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.util.ResourceBundleUtil;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.util.List;


/**
 * Criteria that can be saved in the database (or constructed in the grid service based upon SearchCriteriaDTO)
 *
 * @author dietrichj
 *
 */
public abstract class PersistentCriteria extends Criteria {
    // Prefix that is included with all resource keys
    public static final String DISPLAY_NAME_ID_PREFIX = "criteria_";

    /**
     * Converts the criteria's values into QueryAttributeWrappers
     */
    public abstract List<QueryAttributeWrapper> getQueryAttributes();

    /**
     * Populates the criteria from a QueryAttribute
     *
     * @param attribute
     */
    public abstract void addValueFromQueryAttribute(QueryAttributeWrapper attribute);

    /**
     * Returns a name of this type of criteria to display
     * to the user
     */
    public String getDisplayName() {
        String className = getClass().getName();
        String classNameWithoutPackage = className.substring(className.lastIndexOf(
                    '.') + 1);

        return ResourceBundleUtil.getString(DISPLAY_NAME_ID_PREFIX +
                                            classNameWithoutPackage);
    }


}
