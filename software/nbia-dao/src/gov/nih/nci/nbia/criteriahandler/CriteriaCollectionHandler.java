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
* Revision 1.2  2007/08/29 19:10:55  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:22  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:39  bauerd
* Initial Check in of reorganized components
*
* Revision 1.15  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.criteriahandler;

import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.PatientCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;

import java.util.Collection;
import java.util.Iterator;


/**
 * @author Jin Chen
 *
 *
 *
 */
public class CriteriaCollectionHandler implements CriteriaHandler {

    private static final String wildChar = "*";
    private static final String queryWildChar = "%";

	/**
     * This method will handle the construction of an HQL query for the collection
     * criterias.  It will return a string that shold be placed in the 'WHERE'
     * body of the HQL query.
     */
    public String handle(String field, Criteria inputCrit)
        throws Exception {
        Collection tempCollection = getCriteriaObjects(inputCrit);
        Iterator tempIterator = tempCollection.iterator();
        String tempQueryString = "";

        while (tempIterator.hasNext()) {
            String tempString = null;
            Integer tempInteger = null;
            Object tempObject = null;

            tempObject = tempIterator.next();

            if (tempObject instanceof String) {
                tempString = tempObject.toString();
            } else if (tempObject instanceof Integer) {
                tempInteger = (Integer) tempObject;
            } else {
                throw new Exception("Not supported collection Object: " +
                    tempObject.getClass().getName());
            }

            if (tempQueryString.equals("")) {
                if (tempString != null) {
                	if(tempString.contains(wildChar) && inputCrit instanceof PatientCriteria) {
                		tempQueryString += (" " + field + " like ('" + tempString.replace(wildChar, queryWildChar) +
                				"')");
                	} else {
                		tempQueryString += (" " + field + " IN ('" + tempString +
                				"'");
                	}
                } else if (tempInteger != null) {
                    tempQueryString += (" " + field + " IN ('" + tempInteger +
                    "'");
                }
            } else {
                if (tempString != null) {
                	if(tempString.contains(wildChar) && inputCrit instanceof PatientCriteria) {
                		tempQueryString += (" or (" + field + " like ('" + tempString.replace(wildChar, queryWildChar) +
                				"'))");
                	} else {
                		tempQueryString += (", '" + tempString + "'");
                	}
                } else if (tempInteger != null) {
                    tempQueryString += (", '" + tempInteger + "'");
                }
            }
        }

        tempQueryString += ") ";

        return tempQueryString;
    }

    /**
     * This will retreive the collection of values from the proper criteria
     *
     * @param criteria
     * @throws Exception
     */
    private Collection getCriteriaObjects(Criteria criteria)
        throws Exception {
        Collection tempCollection = null;

        if (criteria instanceof ImageModalityCriteria) {
            tempCollection = ((ImageModalityCriteria) criteria).getImageModalityObjects();
        }
        else if (criteria instanceof UsMultiModalityCriteria) {
            tempCollection = ((UsMultiModalityCriteria) criteria).getUsMultiModalityObjects();
        }
        else if (criteria instanceof CollectionCriteria) {
            tempCollection = ((CollectionCriteria) criteria).getCollectionObjects();
        }
        else if (criteria instanceof PatientCriteria) {
                tempCollection = ((PatientCriteria) criteria).getPatientIdObjects();
        } else if (criteria instanceof ManufacturerCriteria) {
            tempCollection = ((ManufacturerCriteria) criteria).getManufacturerObjects();
        } else if (criteria instanceof ModelCriteria) {
            tempCollection = ((ModelCriteria) criteria).getModelObjects();
        } else if (criteria instanceof SoftwareVersionCriteria) {
            tempCollection = ((SoftwareVersionCriteria) criteria).getSoftwareVersionObjects();
        } else {
            throw new Exception("Not supported collection criteria: " +
                criteria.getClass().getName());
        }

        return tempCollection;
    }
}
