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
import gov.nih.nci.ncia.criteria.UrlParamCriteria;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.OPEN_PARENTHESIS;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.PATIENT_ID;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.AND;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.SERIES_INSTANCE_UID;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.STUDY_INSTANCE_UID;
import static gov.nih.nci.nbia.search.DICOMQueryHandler.CLOSE_PARENTHESIS;

public class UrlParamCriteriaHandler implements CriteriaHandler {
    public String handle(String field, Criteria inputCrit)
        throws Exception {
        UrlParamCriteria crit = (UrlParamCriteria) inputCrit;
        String hqlString = "";
        hqlString += (OPEN_PARENTHESIS + PATIENT_ID + " = '" +
        crit.getPatientId() + "' ");
        hqlString += (AND + SERIES_INSTANCE_UID + " = '" +
        crit.getSeriesInstanceUid() + "' ");
        hqlString += (AND + STUDY_INSTANCE_UID + " = '" +
        crit.getStudyInstanceUid() + "' " + CLOSE_PARENTHESIS);

        return hqlString;
    }
}
