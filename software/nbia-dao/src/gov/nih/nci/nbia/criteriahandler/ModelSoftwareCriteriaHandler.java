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
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.criteriahandler;

import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.nbia.search.DICOMQueryHandler;

import java.util.Collection;


public class ModelSoftwareCriteriaHandler implements CriteriaHandler {
    private boolean isModel;

    public String handle(String field, Criteria inputCrit)
        throws Exception {
        Collection<String> tempCollection = null;

        if (inputCrit instanceof ModelCriteria) {
            isModel = true;
            tempCollection = ((ModelCriteria) inputCrit).getModelObjects();
        } else if (inputCrit instanceof SoftwareVersionCriteria) {
            isModel = false;
            tempCollection = ((SoftwareVersionCriteria) inputCrit).getSoftwareVersionObjects();
        }

        String tempQueryString = "";

        for (String val : tempCollection) {
            if (!tempQueryString.equals("")) {
                tempQueryString += " OR ";
            }

            tempQueryString += ("(" + DICOMQueryHandler.MANUFACTURER_FIELD +
            " = '" + val.split("\\|\\|")[0] + "' AND " +
            DICOMQueryHandler.MODEL_FIELD + " = '" + val.split("\\|\\|")[1] +
            "' ");

            if (!isModel) {
                tempQueryString += (" AND " +
                DICOMQueryHandler.SOFTWARE_VERSION_FIELD + " = '" +
                val.split("\\|\\|")[2] + "' ");
            }

            tempQueryString += ") ";
        }

        return " (" + tempQueryString + ") ";
    }
}
