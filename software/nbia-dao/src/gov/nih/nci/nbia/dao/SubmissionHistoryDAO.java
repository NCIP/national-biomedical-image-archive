/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dao;

import gov.nih.nci.nbia.dto.DayCountDTO;
import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.internaldomain.SubmissionHistory;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;


/**
 * This object encapsulates data access pertaining to the submission_history table.
 * Theoretically would contain all data access CRUD operations, but currently only
 * has "find" operations.
 */
public interface SubmissionHistoryDAO {

	/**
	 * Legal value for operationType
	 */
	public static final int NEW_IMAGE_SUBMISSION_OPERATION = SubmissionHistory.NEW_IMAGE_SUBMISSION_OPERATION;
	public static final int REPLACE_IMAGE_SUBMISSION_OPERATION = SubmissionHistory.REPLACE_IMAGE_SUBMISSION_OPERATION;
	public static final int ANNOTATION_SUBMISSION_OPERATION = SubmissionHistory.ANNOTATION_SUBMISSION_OPERATION;

   	/**
	 * For a given project/site in a date range (inclusive), return an object for each series
	 * that had a submission of the given type (annotation, new image, replace image).
	 *
	 * <p>For each series, the study id, patient id, and count of submissions for that series are
	 * included.
	 *
	 * <p>Theoretically, this method could return quite a bit of data so be careful.
	 */
    public List<SeriesSubmissionCountDTO> findSeriesSubmissionCountInTimeFrame(Date startDate,
    		                                                                   Date endDate,
                                                                               String projectName,
                                                                               String siteName,
                                                                               int operationType) throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * new image submissions.  Also return the number of new image submissions.
    */
   public SubmissionCountsDTO findImageCounts(Date startDate,
                                              Date endDate,
                                              String projectName,
                                              String siteName) throws DataAccessException;


   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * corrected/replace image submissions.  Also return the number of corrected/replaced image submissions.
    */
   public SubmissionCountsDTO findCorrectedCounts(Date startDate,
                                                  Date endDate,
                                                  String projectName,
                                                  String siteName) throws DataAccessException;
   /**
    * For a given project/site in a date range (inclusive), find the number
    * of distinct patient id/study id/series id that were associated with
    * annotation submissions.  Also return the number of annoatation submissions.
    */
   public SubmissionCountsDTO findAnnotationCounts(Date startDate,
                                                   Date endDate,
                                                   String projectName,
                                                   String siteName) throws DataAccessException;
   /**
    * For a given project/site in a date range (inclusive), return the number
    * of new image submissions.
    */
   public long findImageSubmissionCountInTimeFrame(Date startDate,
                                                   Date endDate,
                                                   String projectName,
                                                   String siteName) throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of series that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" series.
    */
   public int findUpdatedSeriesCountInTimeFrame(Date startDate,
                                                Date endDate,
                                                String projectName,
                                                String siteName) throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of series that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" series.
    */
   public int findNewSeriesCountInTimeFrame(Date startDate,
                                            Date endDate,
                                            String projectName,
                                            String siteName)throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of studies that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" studies.
    */
   public int findUpdatedStudyCountInTimeFrame(Date startDate,
                                               Date endDate,
                                               String projectName,
                                               String siteName)throws DataAccessException;
   /**
    * For a given project/site in a date range (inclusive), find the number
    * of studies that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" studies.
    */
   public int findNewStudyCountInTimeFrame(Date startDate,
                                           Date endDate,
                                           String projectName,
                                           String siteName) throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of patients that had a submission before the time frame, and a submission in the
    * time frame, aka the count of "updated" patients.
    */
   public int findUpdatedPatientCountInTimeFrame(Date startDate,
                                                 Date endDate,
                                                 String projectName,
                                                 String siteName) throws DataAccessException;

   /**
    * For a given project/site in a date range (inclusive), find the number
    * of patients that DID NOT have a submission before the time frame, and a
    * they DO have a submission in the time frame, aka the count of "new" patients.
    */
   public int findNewPatientCountInTimeFrame(Date startDate,
                                             Date endDate,
                                             String projectName,
                                             String siteName) throws DataAccessException;


   /**
    * For a given project/site in a date range (inclusive), find the days where
    * a submission of the given type occurred.  The day returned will be associated
    * with the count of submissions for that day (the sum of all the specified
    * operation types on that day).
    */
   public List<DayCountDTO> findSubmissionDatesInTimeFrame(Date startDate,
                                                           Date endDate,
                                                           String projectName,
                                                           String siteName,
                                                           Integer[] operationTypes) throws DataAccessException;



   /**
    * For a given project/site in a date range (inclusive), return the number
    * of annotation submissions.
    */
    public long findAnnotationSubmissionCountInTimeFrame(Date startDate,
                                                         Date endDate,
                                                         String projectName,
                                                         String siteName) throws DataAccessException;
}
