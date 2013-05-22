/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.util.SiteData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Standalone, reusable, motley collection of methods used by the submission report feature
 */
public class VerifySubmissionUtil {
	/**
	 * Parse a date string into an object.
	 */
	public static Date dateParse(String date) throws Exception {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.parse(date);
    }

	/**
	 * Format a Date object into a string format for a day that
	 * meets submission report standard.
	 */
	public static String dateFormat(Date date) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }

	/**
	 * Given a project//site string, return the project part.
	 */
	public static String getProject(String projectSite) {
    	return projectSite.substring(0, projectSite.indexOf(SiteData.SITE_DELIMITER));
    }


	/**
	 * Given a project//site string, return the site part.
	 */
	public static String getSite(String projectSite) {
    	int dex = projectSite.indexOf(SiteData.SITE_DELIMITER);
    	return projectSite.substring(dex+SiteData.SITE_DELIMITER.length());
    }


	/**
	 * Given a SiteData, spit out the proper project//site string
	 */
	public static String siteDataToString(SiteData siteData) {
    	return siteData.getCollection() + SiteData.SITE_DELIMITER + siteData.getSiteName();
    }

	public static String siteDataToString(String project, String site) {
    	return project + SiteData.SITE_DELIMITER + site;
    }


	/**
	 * Take the linear collection of SeriesSubmissionCountDTO and turn into hierarchical
	 * data structure that is patient-centric.
	 */
	public static List<PatientDetails> constructPatientDetails(List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs) {
		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();

		Map<String, List<SeriesDetails>> studyToSeriesMap = new HashMap<String,List<SeriesDetails>>();
		Map<String, Set<String>> patientToStudyMap = new HashMap<String,Set<String>>();

		for(SeriesSubmissionCountDTO seriesSubmissionCountDTO : seriesSubmissionCountDTOs) {
			SeriesDetails seriesDetails = new SeriesDetails(seriesSubmissionCountDTO.getSeriesInstanceUid(),
					                                        seriesSubmissionCountDTO.getSubmissionCount());

			List<SeriesDetails> seriesDetailsList = studyToSeriesMap.get(seriesSubmissionCountDTO.getStudyInstanceUid());
			if(seriesDetailsList==null) {
				seriesDetailsList = new ArrayList<SeriesDetails>();
				studyToSeriesMap.put(seriesSubmissionCountDTO.getStudyInstanceUid(),
						             seriesDetailsList);
			}
			seriesDetailsList.add(seriesDetails);

			Set<String> studyIds = patientToStudyMap.get(seriesSubmissionCountDTO.getPatientId());
			if(studyIds==null) {
				studyIds = new HashSet<String>();
				patientToStudyMap.put(seriesSubmissionCountDTO.getPatientId(),
				                      studyIds);
			}
			studyIds.add(seriesSubmissionCountDTO.getStudyInstanceUid());//fine to duplicate since set
		}

		for(String patientId : patientToStudyMap.keySet()) {
			Set<String> studyIds = patientToStudyMap.get(patientId);


			List<StudyDetails> studyDetailsList = new ArrayList<StudyDetails>();
			for(String studyId : studyIds) {
				StudyDetails studyDetails = new StudyDetails(studyId,
				                                             studyToSeriesMap.get(studyId));
				studyDetailsList.add(studyDetails);
			}

			PatientDetails patientDetails = new PatientDetails(patientId, studyDetailsList);

			patientDetailsList.add(patientDetails);
		}

		return patientDetailsList;
	}
}
