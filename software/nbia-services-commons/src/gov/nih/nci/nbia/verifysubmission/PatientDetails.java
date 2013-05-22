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
 * This object corresponds to a Patient object.  It includes
 * the patient id and an aggregation of counts for the number
 * of studies, series, and images associated with it.  Further,
 * it contains a collection of study details for each
 * study associated with the patient.
 * 
 * <p>This object is meant to be immutable.
 */
public class PatientDetails {
	public PatientDetails(String patientId, 
			              List<StudyDetails> theStudyDetails) {
		this.patientId = patientId;
		this.studyDetails = theStudyDetails;
		
		if(studyDetails==null||studyDetails.size()<1) {
			throw new RuntimeException("patient should have 1 or more studies");
		}		
	}
	

    public String getPatientId() {
    	return patientId;
    }
    
    /**
     * The studies directly associated with this patient.
     */
    public List<StudyDetails> getStudyDetails() {
    	return Collections.unmodifiableList(studyDetails);
    }
    
    /**
     * The number of studies associated with this patient.
     */
    public int getStudyCount() {
    	return studyDetails.size();
    }
    
    /**
     * The number of series associated with this patient.
     * This is an aggregate computed by asking each associated
     * study how many series it (directly) has.
     */
    public int getSeriesCount() {
    	int total = 0 ;
    
    	for(StudyDetails studyDetail : studyDetails) {
    		total += studyDetail.getSeriesCount();
    	}
    	return total;
    }
    
    /**
     * The number of submissions associated with this patient. The
     * submission could be new image, image replacement, or annotation etc.
     * This is an aggregate computed by asking each associated
     * study and it turn each series how many submission they have.
     */    
    public int getSubmissionCount() {
    	int total = 0 ;
    
    	for(StudyDetails studyDetail : studyDetails) {
    		List<SeriesDetails> seriesDetails = studyDetail.getSeriesDetails();
    		for(SeriesDetails seriesDetail : seriesDetails) {
    			total += seriesDetail.getSubmissionCount();
    		}
    	}
    	return total;
    }        
    
    ///////////////////////////////////PRIVATE///////////////////////////////
    private String patientId;

    /**
     * does it really make sense for this to have order (by a List) or
     * would set be more appropriate?
     */
    private List<StudyDetails> studyDetails;
}

