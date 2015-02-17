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
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.2  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.criteriahandler;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.RangeCriteria;


/**
 * Builds HQL for a range of valies
 *
 *
 * @author NCIA Team
 */
public class RangeCriteriaHandler implements CriteriaHandler {

    /**
     * This method will handle the construction of an HQL query for the collection
     * criterias.  It will return a string that shold be placed in the 'WHERE'
     * body of the HQL query.
     */
    public String handle(String field, Criteria inputCrit)
        throws Exception {
        RangeCriteria rc = (RangeCriteria) inputCrit;
        String hqlStmt = "and " + field + rc.getRangeData().getFromOperator() +
            " " + rc.getRangeData().getFromValue() + " ";

        String toOperator = rc.getRangeData().getToOperator();

        if ((toOperator != null) && !toOperator.equals("")) {
            hqlStmt += ("and " + field + rc.getRangeData().getToOperator() +
            " " + rc.getRangeData().getToValue() + " ");
        }

        return hqlStmt;
    }
}
