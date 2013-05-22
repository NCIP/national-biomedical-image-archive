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
* Revision 1.1  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.querystorage;


/**
 * Interface for a domain class that represents
 * a query that is saved to the database.
 *
 *
 * @author NCIA Team
 */
public interface PersistentQuery {
    public void addQueryAttribute(QueryAttributeWrapper attr, int instanceNumber);
}
