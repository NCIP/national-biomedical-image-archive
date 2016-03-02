/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

/**
 * This object corresponds to a GeneralSeries object.  It includes
 * the series instance uid and a count of images associated with it
 * 
 * <p>This object is meant to be immutable.
 */
public class SeriesDetails {
	
	public SeriesDetails(String seriesInstanceUid, long submissionCount) {
		this.seriesInstanceUid = seriesInstanceUid;
		this.submissionCount = submissionCount;
	}
	
	
    public String getSeriesInstanceUid() {
    	return seriesInstanceUid;
    }
    
    
    public long getSubmissionCount() {
    	return submissionCount;
    }
    
    private String seriesInstanceUid;

    private long submissionCount;
} 
