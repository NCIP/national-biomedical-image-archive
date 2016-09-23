/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import gov.nih.nci.nbia.qctool.VisibilityStatus;

import java.text.SimpleDateFormat;
import java.util.Date;	

public class QcStatusHistoryDTO {
	private Date timeStamp;	
	private String series;
	private String newStatus;
	private String oldStatus;
	
	private String oldBatch;
	private String newBatch;
	private String oldSubmissionType;
	private String newSubmissionType;
	private String oldReleasedStatus;
	private String newReleasedStatus;
	
	private String comment;
	private String userId;
	
	public QcStatusHistoryDTO(Date timeStamp, 
					   String series,
					   String newStatus,
					   String oldStatus,
					   
					   String oldBatch,
					   String newBatch,
					   String oldSubmissionType,
					   String newSubmissionType,	
					   String oldReleasedStatus,
					   String newReleasedStatus,
				
					   String comment,
					   String userId) {
		setTimeStamp(timeStamp);
		setSeries(series);
		setNewStatus(newStatus);
		setOldStatus(oldStatus);
		
		setOldBatch(oldBatch);
		setNewBatch(newBatch);
		setOldSubmissionType(oldSubmissionType);
		setNewSubmissionType(newSubmissionType);
		
		setOldReleasedStatus(oldReleasedStatus);
		setNewReleasedStatus(newReleasedStatus);
		
		setComment(comment);
		setUserId(userId);
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the series
	 */
	public String getSeries() {
		return series;
	}

	/**
	 * @param series the series to set
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * @return the newStatus
	 */
	public String getNewStatus() {
		return newStatus;
	}
	
	public String getNewVisibilityStatus() {
		return VisibilityStatus.statusFactory(Integer.parseInt(newStatus)).getText();
	}

	/**
	 * @param newStatus the newStatus to set
	 */
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	/**
	 * @return the oldStatus
	 */
	public String getOldStatus() {
		return oldStatus;
	}
	
	public String getOldVisibilityStatus() {
		return VisibilityStatus.statusFactory(Integer.parseInt(oldStatus)).getText();
	}

	/**
	 * @param oldStatus the oldStatus to set
	 */
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
		String dateTime = sdf.format(timeStamp);
		return dateTime;
	}

	public String getOldBatch(){
		return oldBatch;
	}
	
	public void setOldBatch(String oldBatch){
		this.oldBatch = oldBatch;
	}
	
	public String getNewBatch(){
		return newBatch;
	}
	
	public void setNewBatch(String newBatch){
		this.newBatch = newBatch;
	}
	
	public String getOldSubmissionType(){
		return oldSubmissionType;
	}
	
	public void setOldSubmissionType(String oldSubmissionType){
		this.oldSubmissionType = oldSubmissionType;
	}
	
	public String getNewSubmissionType(){
		return newSubmissionType;
	}
	
	public void setNewSubmissionType(String newSubmissionType){
		this.newSubmissionType = newSubmissionType;
	}
	
	//--------------------------------------------------------------
	public String getOldReleasedStatus(){
		return oldReleasedStatus;
	}
	
	public void setOldReleasedStatus(String oldReleasedStatus){
		this.oldReleasedStatus = oldReleasedStatus;
	}
	
	public String getNewReleasedStatus(){
		return newReleasedStatus;
	}
	
	public void setNewReleasedStatus(String newReleasedStatus){
		this.newReleasedStatus = newReleasedStatus;
	}
	
  //---------------------------------------------------------------------
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
