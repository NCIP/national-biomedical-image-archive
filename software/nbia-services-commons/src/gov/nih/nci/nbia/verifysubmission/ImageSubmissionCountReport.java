/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

/**
 * This is a piece of the ImageSubmissionReport that captures
 * all the counts.
 * 
 * <p>This object is meant to be immutable.
 */
public class ImageSubmissionCountReport {
	/**
	 * Constructor.  All parameters feed through here
	 * to avoid setters that would violate immutability.
	 */
	public ImageSubmissionCountReport(long affectedPatientCount,
			                          long affectedStudyCount, 
			                          long affectedSeriesCount,
			                          long submittedImageCount, 
			                          long correctedPatientCount,
			                          long correctedStudyCount, 
			                          long correctedSeriesCount,
			                          long correctedImageCount) {
		this.affectedPatientCount = affectedPatientCount;
		this.affectedStudyCount = affectedStudyCount;
		this.affectedSeriesCount = affectedSeriesCount;
		this.submittedImageCount = submittedImageCount;
		this.correctedPatientCount = correctedPatientCount;
		this.correctedStudyCount = correctedStudyCount;
		this.correctedSeriesCount = correctedSeriesCount;
		this.correctedImageCount = correctedImageCount;
	}

	public long getSubmittedImageCount() {
		return submittedImageCount;
	}


	public long getAffectedPatientCount() {
		return affectedPatientCount;
	}


	public long getAffectedStudyCount() {
		return affectedStudyCount;
	}


	public long getAffectedSeriesCount() {
		return affectedSeriesCount;
	}


	public long getCorrectedPatientCount() {
		return correctedPatientCount;
	}


	public long getCorrectedStudyCount() {
		return correctedStudyCount;
	}


	public long getCorrectedSeriesCount() {
		return correctedSeriesCount;
	}


	public long getCorrectedImageCount() {
		return correctedImageCount;
	}

	
	private long affectedPatientCount;

	private long affectedStudyCount;

	private long affectedSeriesCount;	
	
	private long submittedImageCount;
	
	private long correctedPatientCount;

	private long correctedStudyCount;

	private long correctedSeriesCount;	
	
	private long correctedImageCount;	
	
}
