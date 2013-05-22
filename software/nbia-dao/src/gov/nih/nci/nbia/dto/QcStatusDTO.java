/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.util.Date;	

public class QcStatusDTO {
	private Date   timeStamp;	
	private	String series;
	private String newStatus;
	private String oldStatus;
	
	public QcStatusDTO(Date timeStamp, 
					   String series,
					   String newStatus,
					   String oldStatus) {
		setTimeStamp(timeStamp);
		setSeries(series);
		setNewStatus(newStatus);
		setOldStatus(oldStatus);
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

	/**
	 * @param oldStatus the oldStatus to set
	 */
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getDateTime() {
		return timeStamp.toString();
	}
}
