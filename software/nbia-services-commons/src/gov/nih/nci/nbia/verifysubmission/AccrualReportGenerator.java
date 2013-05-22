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
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This object does the heavy lifting to generate an accrual report.
 * It talks to a variety of DAO objects to get the necessary info
 * from the database, and then correlates the information in the
 * report
 */
public class AccrualReportGenerator {

	/**
	 * Generate the report for just a single day (instead of a
	 * day range)
	 */
	@Transactional(propagation=Propagation.REQUIRED)
    public AccrualReport generateReportByDay(Date day,
                                             String projectName,
                                             String siteName) {
        return generateReport(day, day, projectName, siteName);        
    }
    
    /**
     * Generate the accrual report for a given collection/site
     * within a day range (inclusive).
     */
    @Transactional(propagation=Propagation.REQUIRED)	
    public AccrualReport generateReport(Date startDate,
                                        Date endDate,                             
                                        String projectName,
                                        String siteName) {
        
        
		SubmissionHistoryDAO submissionHistoryDAO = (SubmissionHistoryDAO)SpringApplicationContext.getBean("submissionHistoryDAO");
        
        
        SubmissionCountsDTO correctedCountsDTO = submissionHistoryDAO.findCorrectedCounts(startDate, 
                                                                                         endDate,
                                                                                         projectName,
                                                                                         siteName);
        
        int newPatients = submissionHistoryDAO.findNewPatientCountInTimeFrame(startDate, 
                                                                              endDate, 
                                                                              projectName, 
                                                                              siteName);
        
        int newStudies = submissionHistoryDAO.findNewStudyCountInTimeFrame(startDate, 
                                                                           endDate, 
                                                                           projectName, 
                                                                           siteName);
        
        int newSeries = submissionHistoryDAO.findNewSeriesCountInTimeFrame(startDate, 
                                                                           endDate, 
                                                                           projectName, 
                                                                           siteName);
        
        int updatedPatients = submissionHistoryDAO.findUpdatedPatientCountInTimeFrame(startDate, 
                                                                                      endDate, 
                                                                                      projectName, 
                                                                                      siteName);
        
        int updatedStudies = submissionHistoryDAO.findUpdatedStudyCountInTimeFrame(startDate, 
                                                                                   endDate, 
                                                                                   projectName, 
                                                                                   siteName);
        
        int updatedSeries = submissionHistoryDAO.findUpdatedSeriesCountInTimeFrame(startDate, 
                                                                                   endDate, 
                                                                                   projectName, 
                                                                                   siteName);

        long imageCnt = submissionHistoryDAO.findImageSubmissionCountInTimeFrame(startDate,
                                                                                 endDate,
                                                                                 projectName,
                                                                                 siteName);
        
        
        List<DayCountDTO> submissionDays = submissionHistoryDAO.findSubmissionDatesInTimeFrame(startDate,
                                                                                        endDate, 
                                                                                        projectName, 
                                                                                        siteName, 
                                                                                        new Integer[] {SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION,
        		                                                                                       SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION}); 
        
        SortedMap<Date,Integer> submissionDaysMap = new TreeMap<Date,Integer>();
        for(DayCountDTO dayCountDTO : submissionDays) {
        	submissionDaysMap.put(dayCountDTO.getDay(), dayCountDTO.getSubmissionCount());
        }
        
        AccrualReport accrualReport = new AccrualReport(newPatients,
                                                        updatedPatients,
                                                        correctedCountsDTO.getPatientCount(),
                                                        newStudies,
                                                        updatedStudies,
                                                        correctedCountsDTO.getStudyCount(),
                                                        newSeries,
                                                        updatedSeries,
                                                        correctedCountsDTO.getSeriesCount(),
                                                        correctedCountsDTO.getSubmissionCount(),
                                                        imageCnt,
                                                        submissionDaysMap,
                                                        startDate,
                                                        endDate,
                                                        VerifySubmissionUtil.siteDataToString(projectName, siteName));
        
        
        return accrualReport;
    }
    
}
