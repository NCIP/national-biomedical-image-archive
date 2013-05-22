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
* Revision 1.7  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.criteriahandler;

import gov.nih.nci.ncia.criteria.Criteria;


/**
 * Constructs a WHERE clause for an HQL query. The Criteria object just holds
 * the values that are used to construct the actual HQL here.
 * 
 * @author Jin Chen
 */
public interface CriteriaHandler {
	/**
	 * Return the HQL WHERE clause for the given criteria.  The field name
	 * is for the field that the criteria applies to.  Sometimes this is used
	 * to construct the actual HQL, sometimes it isn't....
	 */
    public String handle(String field, Criteria inputCrit) throws Exception;
}
