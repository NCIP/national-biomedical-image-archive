/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

/**
 * This is a DTO passed back by the SubmissionHistoryDAO.  It encapsulates
 * a series uid and its associted study and patient ids.  Further, it
 * associates a submission count for that series... the number of submissions
 * that happened against the series (what kind of submmsion, image or annotation, etc.
 * will depend on the DAO operation invoked).
 * 
 * <p>This DTO is meant to be immutable.  i.e. no mutators/setters to screw with state.
 */
public class SeriesSubmissionCountDTO {

	
	public SeriesSubmissionCountDTO(String patientId, 
			                        String studyInstanceUid,
			                        String seriesInstanceUid, 
			                        long submissionCount) {
		super();
		this.patientId = patientId;
		this.studyInstanceUid = studyInstanceUid;
		this.seriesInstanceUid = seriesInstanceUid;
		this.submissionCount = submissionCount;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	
	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}
	
	
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	
	
	public long getSubmissionCount() {
		return submissionCount;
	}
	
	private String patientId;
    private String studyInstanceUid;
	private String seriesInstanceUid;
	private long submissionCount;//image or annotation
}