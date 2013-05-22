/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import gov.nih.nci.nbia.dao.SubmissionHistoryDAO;
import gov.nih.nci.nbia.dto.DayCountDTO;
import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This object generates all the details for an image submission report.
 * This includes the overall counts for a time frame, and then
 * the per day details of image submission.
 */
public class ImageSubmissionReportGenerator {

	/**
	 * Return all patients/study/series that are affected by/associated
	 * with NEW submissions on a given day
	 */
    @Transactional(propagation=Propagation.REQUIRED)	
	public List<PatientDetails> generateAffectedReportByDay(Date day,
                                                            String projectName,
                                                            String siteName) {

		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");


		List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs =
			submissionHistoryDAO.findSeriesSubmissionCountInTimeFrame(day,
					                                                  day,
					                                                  projectName,
					                                                  siteName,
					                                                  SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION);

		List<PatientDetails> patientDetailsList = VerifySubmissionUtil.constructPatientDetails(seriesSubmissionCountDTOs);

		return patientDetailsList;

	}

	/**
	 * Return all patients/study/series that are affected by/associated
	 * with CORRECTION/replacement submissions on a given day
	 */
    @Transactional(propagation=Propagation.REQUIRED)
	public List<PatientDetails> generateCorrectedReportByDay(Date day,
                                                             String projectName,
                                                             String siteName) {

		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");


		List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs =
			submissionHistoryDAO.findSeriesSubmissionCountInTimeFrame(day,
					                                                  day,
					                                                  projectName,
					                                                  siteName,
					                                                  SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION);

		List<PatientDetails> patientDetailsList = VerifySubmissionUtil.constructPatientDetails(seriesSubmissionCountDTOs);

		return patientDetailsList;

	}

    /**
     * Generate the image submission report for a given collection/site
     * within a day range (inclusive).
     */	
    @Transactional(propagation=Propagation.REQUIRED)	
	public ImageSubmissionReport generateReport(Date startDate,
			                                    Date endDate,
			                                    String projectName,
			                                    String siteName) {


		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");

		SubmissionCountsDTO imageSubmissionCountsDTO = submissionHistoryDAO.findImageCounts(startDate,
				                                                                            endDate,
				                                                                            projectName,
				                                                                            siteName);

		SubmissionCountsDTO correctedCountsDTO = submissionHistoryDAO.findCorrectedCounts(startDate,
			                                                                              endDate,
				                                                                          projectName,
				                                                                          siteName);

		List<DayCountDTO> submissionDays = submissionHistoryDAO.findSubmissionDatesInTimeFrame(startDate,
				                                                                               endDate,
				                                                                               projectName,
				                                                                               siteName,
				                                                                               new Integer[]{SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION}); 


		List<DayCountDTO> correctedSubmissionDays = submissionHistoryDAO.findSubmissionDatesInTimeFrame(startDate,
				                                                                                        endDate,
				                                                                                        projectName,
				                                                                                        siteName,
				                                                                                        new Integer[]{SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION}); 

		ImageSubmissionCountReport imageSubmissionCountReport =
		    new ImageSubmissionCountReport(imageSubmissionCountsDTO.getPatientCount(),
                                           imageSubmissionCountsDTO.getStudyCount(),
                                           imageSubmissionCountsDTO.getSeriesCount(),
                                           imageSubmissionCountsDTO.getSubmissionCount(),
                                           correctedCountsDTO.getPatientCount(),
                                           correctedCountsDTO.getStudyCount(),
                                           correctedCountsDTO.getSeriesCount(),
                                           correctedCountsDTO.getSubmissionCount());

        SortedMap<Date,Integer> submissionDaysMap = new TreeMap<Date,Integer>();
        for(DayCountDTO dayCountDTO : submissionDays) {
        	submissionDaysMap.put(dayCountDTO.getDay(), dayCountDTO.getSubmissionCount());
        }

        SortedMap<Date,Integer> correctedSubmissionDaysMap = new TreeMap<Date,Integer>();
        for(DayCountDTO dayCountDTO : correctedSubmissionDays) {
        	correctedSubmissionDaysMap.put(dayCountDTO.getDay(), dayCountDTO.getSubmissionCount());
        }

		ImageSubmissionReport imageSubmissionReport = new ImageSubmissionReport(imageSubmissionCountReport,
				                                                                submissionDaysMap,
				                                                                correctedSubmissionDaysMap,
				                                                                startDate,
				                                                                endDate,
				                                                                VerifySubmissionUtil.siteDataToString(projectName, siteName));


		return imageSubmissionReport;
	}
}
