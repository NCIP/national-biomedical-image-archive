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
 * This object does the heavy lifting to generate an annotation submission report.
 * It talks to a variety of DAO objects to get the necessary info
 * from the database, and then correlates the information in the
 * report
 */
public class AnnotationReportGenerator {

	/**
	 * Generate the report for just a single day (instead of a
	 * day range)
	 */	
    @Transactional(propagation=Propagation.REQUIRED)
	public List<PatientDetails> generateReportByDay(Date day,
                                                    String projectName,
                                                    String siteName) {

		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");
		
		
		List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs = 
			submissionHistoryDAO.findSeriesSubmissionCountInTimeFrame(day,
					                                                  day,
					                                                  projectName,
					                                                  siteName,
					                                                  2);	
		
		List<PatientDetails> patientDetailsList = VerifySubmissionUtil.constructPatientDetails(seriesSubmissionCountDTOs);

		return patientDetailsList;		
	
	}
	
    /**
     * Generate the annotation submission report for a given collection/site
     * within a day range (inclusive).
     */
    @Transactional(propagation=Propagation.REQUIRED)    
	public AnnotationReport generateReport(Date startDate,
			                               Date endDate,			                 
			                               String projectName,
			                               String siteName) {
		
		
		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");
		
		SubmissionCountsDTO submissionCountsDTO = submissionHistoryDAO.findAnnotationCounts(startDate, 
				                                                                            endDate, 
				                                                                            projectName, 
				                                                                            siteName);
		
		List<DayCountDTO> submissionDays = submissionHistoryDAO.findSubmissionDatesInTimeFrame(startDate,
				                                                                        endDate, 
				                                                                        projectName, 
				                                                                        siteName, 
				                                                                        new Integer[]{SubmissionHistoryDAO.ANNOTATION_SUBMISSION_OPERATION}); //make const

		
		AnnotationCountReport annotationCountReport = new AnnotationCountReport(submissionCountsDTO.getPatientCount(),
                                                                                submissionCountsDTO.getStudyCount(),
                                                                                submissionCountsDTO.getSeriesCount(),
                                                                                submissionCountsDTO.getSubmissionCount());

		
        SortedMap<Date,Integer> submissionDaysMap = new TreeMap<Date,Integer>();
        for(DayCountDTO dayCountDTO : submissionDays) {
        	submissionDaysMap.put(dayCountDTO.getDay(), dayCountDTO.getSubmissionCount());
        }		
		
		AnnotationReport annotationReport = new AnnotationReport(annotationCountReport,
				                                                 new TreeMap<Date, Integer>(submissionDaysMap),
				                                                 startDate,
  		                                                         endDate,
  		                                                         VerifySubmissionUtil.siteDataToString(projectName, siteName));
		
		
		return annotationReport;
	}	
}
