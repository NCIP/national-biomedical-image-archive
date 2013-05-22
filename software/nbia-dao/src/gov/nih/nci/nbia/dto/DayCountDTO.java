/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.util.Date;

/**
 * This is a DTO passed back by the SubmissionHistoryDAO.  It encapsulates
 * a day and a submission count for that day (whether it be image submissions,
 * annotation submissions, etc. it's up to the caller to know that based
 * upon the find operation invoked)
 * 
 * <p>This DTO is meant to be immutable.  i.e. no mutators/setters to screw with state.
 */
public class DayCountDTO {
	public DayCountDTO(Date day, int submissionCount) {
		super();
		this.day = day;
		this.submissionCount = submissionCount;
	}
	
	/**
	 * This is a day - any time information in the Date should
	 * be disregarded.
	 */
	public Date getDay() {
		return day;
	}
	
	/**
	 * Submission count for the given day.
	 */
	public int getSubmissionCount() {
		return submissionCount;
	}
	
	private Date day;
	
	private int submissionCount;
}
