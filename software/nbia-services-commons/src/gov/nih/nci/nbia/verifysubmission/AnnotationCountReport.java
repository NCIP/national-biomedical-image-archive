/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

/**
 * This is a piece of the AnnotationReport that captures
 * all the counts.
 * 
 * <p>This object is meant to be immutable.
 */
public class AnnotationCountReport {
	/**
	 * Constructor.  All parameters feed through here
	 * to avoid setters that would violate immutability.
	 */	
	public AnnotationCountReport(long affectedPatientCount, 
			                     long affectedStudyCount,
			                     long affectedSeriesCount,
			                     long annotationSubmissionCount) {
		this.affectedPatientCount = affectedPatientCount;
		this.affectedStudyCount = affectedStudyCount;
		this.affectedSeriesCount = affectedSeriesCount;
		this.annotationSubmissionCount = annotationSubmissionCount;
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
	
	public long getAnnotationSubmissionCount() {
		return annotationSubmissionCount;
	}
	

	private long affectedPatientCount;

	private long affectedStudyCount;

	private long affectedSeriesCount;
	
	private long annotationSubmissionCount;
}
