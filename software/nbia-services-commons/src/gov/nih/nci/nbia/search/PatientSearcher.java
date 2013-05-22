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
* Revision 1.5  2008/01/12 08:34:06  bauerd
* *** empty log message ***
*
* Revision 1.4  2007/12/28 21:15:21  bauerd
* modifications required to handle a collection across the grid
*
* Revision 1.3  2007/09/27 23:32:22  bauerd
* *** empty log message ***
*
* Revision 1.2  2007/08/07 12:05:23  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.46  2006/11/27 18:11:08  shinohaa
* grid functionality
*
* Revision 1.45  2006/11/27 16:51:42  panq
* Modified for getting thumbnails from the grid.
*
* Revision 1.44  2006/11/15 15:40:47  shinohaa
* grid prototype
*
* Revision 1.43  2006/11/10 13:59:52  shinohaa
* grid prototype
*
* Revision 1.42  2006/10/10 18:48:39  shinohaa
* 2.1 enhancements
*
* Revision 1.41  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Aug 9, 2005
 * Prashant Shah SAIC/NCICB
 *
 *
 *
 */
package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dto.StudyNumberDTO;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 *
 * A wrapper class that excutes the query and returns the appropriate List to the JSF pages
 */
public class PatientSearcher {

	/**
	 * Find patients based upon the criteria in the DICOMQuery.
	 * This is main point of search for the first page of results.
	 * (the artist formerly known as ResultSetManager).
	 */
    public List<PatientSearchResult> searchForPatients(DICOMQuery query) throws Exception {

        Map<Integer, PatientSearchResultImpl> patients = new HashMap<Integer, PatientSearchResultImpl>();
        long startTime = System.currentTimeMillis();

        // Run the query
        DICOMQueryHandler dqh = (DICOMQueryHandler)SpringApplicationContext.getBean("dicomQueryHandler");
        List<PatientStudySeriesTriple> resultSets = dqh.findTriples(query);

        Iterator<PatientStudySeriesTriple> iter = resultSets.iterator();

        while (iter.hasNext()) {
            PatientStudySeriesTriple prs = (PatientStudySeriesTriple) iter.next();

            Integer patientId = prs.getPatientPkId();
            Integer studyId = prs.getStudyPkId();
            Integer seriesId = prs.getSeriesPkId();

            // Look to see if patient has already been encountered
            PatientSearchResultImpl patient = patients.get(patientId);

            if (patient != null) {
                // Patient is already in list, just add series data
                patient.addSeriesForStudy(studyId, seriesId);
            } else {
                // Need to add the patient to the list
            	PatientSearchResultImpl patientDTO = new PatientSearchResultImpl();
                patientDTO.setId(prs.getPatientPkId());

                // Add the series and study to the list
                patientDTO.addSeriesForStudy(studyId, seriesId);

                // Get the other patient data from the cache
                StudyNumberDTO cachedPatientData = ApplicationFactory.getInstance().getStudyNumberMap().getStudiesForPatient(prs.getPatientPkId());
                patientDTO.setTotalNumberOfStudies(cachedPatientData.getStudyNumber());
                patientDTO.setTotalNumberOfSeries(cachedPatientData.getSeriesNumber());
                patientDTO.setSubjectId(cachedPatientData.getPatientId());
                patientDTO.setProject(cachedPatientData.getProject());

                patientDTO.associateLocation(LocalNode.getLocalNode());

                patients.put(patientId, patientDTO);
            }
        }

        // Convert to a list and sort prior to returning
        List<PatientSearchResult> returnList = new ArrayList<PatientSearchResult>(patients.values());
        Collections.sort(returnList);

        long elapsedTime = System.currentTimeMillis() - startTime;
        query.setElapsedTimeInMillis(elapsedTime);
        logger.info("Results returned and built into DTOs in " + elapsedTime +" ms.");

        return returnList;
    }

    /////////////////////////////////////PRIVATE//////////////////////////////////////////

    private static Logger logger = Logger.getLogger(PatientSearcher.class);
}
