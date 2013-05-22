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
* Revision 1.5  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.criteria;


/**
 * Criteria that is not saved to the database
 *
 * @author dietrichj
 *
 */
public abstract class TransientCriteria extends Criteria {
    /**
     * Returns a string to display to the user representing
     * this criteria's value
     */
    public String getDisplayValue() {
        // Transient criteria is never displayed to the user
        // but could be in the future.
        return "";
    }

    /**
     * Returns a name of this type of criteria to display
     * to the user
     */
    public String getDisplayName() {
        // Transient criteria is never displayed to the user
        // but could be in the future.		
        return "";
    }
}
