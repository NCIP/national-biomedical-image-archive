/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import java.util.Collections;
import java.util.List;

/**
 * This object corresponds to a Study object.  It includes
 * the study instance uid and an aggregation of counts for the number
 * of series, and images associated with it.  Further,
 * it contains a collection of series details for each
 * series associated with the study.
 * 
 * <p>This object is meant to be immutable.
 */
public class StudyDetails {
	public StudyDetails(String studyInstanceUid, 
			            List<SeriesDetails> seriesDetails) {
		this.studyInstanceUid = studyInstanceUid;
		this.seriesDetails = seriesDetails;
		
		if(seriesDetails==null||seriesDetails.size()<1) {
			throw new RuntimeException("study should have 1 or more series");
		}
	}
	
    public String getStudyInstanceUid() {
    	return studyInstanceUid;
    }
    
    
    /**
     * The series directly associated with this study.
     */    
    public List<SeriesDetails> getSeriesDetails() {
    	return Collections.unmodifiableList(seriesDetails);
    }
    
    /**
     * The number of series associated with this study.
     */    
    public int getSeriesCount() {
    	return seriesDetails.size();
    }
    
    /**
     * The number of submissions associated with this study. The
     * submission could be new image, image replacement, or annotation etc.
     * This is an aggregate computed by asking each associated
     * series how many submission they have.
     */     
    public int getSubmissionCount() {
    	int total = 0 ;
    
    	for(SeriesDetails seriesDetail : seriesDetails) {
 			total += seriesDetail.getSubmissionCount();
    	}
    	return total;
    }        
    
    private String studyInstanceUid;
    
    private List<SeriesDetails> seriesDetails;   	
}

