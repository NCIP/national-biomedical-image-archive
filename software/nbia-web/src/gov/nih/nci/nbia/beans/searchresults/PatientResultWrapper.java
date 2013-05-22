/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.ncia.search.PatientSearchResult;


public class PatientResultWrapper {
	public PatientResultWrapper(PatientSearchResult patient) {
		this.patient = patient;
	}
	
	public PatientSearchResult getPatient() {
		return patient;
	}

	
	public String getBasketKey() {
		return patient.getSubjectId() + "||" + patient.associatedLocation().getURL();
	}
	
    
    /**
     * Get the study counts for display. This includes the filtered number and
     * the total number.
     */ 
    public String getStudyCounts() {
        return patient.computeFilteredNumberOfStudies() + " / " + patient.getTotalNumberOfStudies();
    }

    /**
     * Get the study counts for display. This includes the filtered number and
     * the total number.
     */ 
    public String getSeriesCounts() {
        return patient.computeFilteredNumberOfSeries() + " / " + patient.getTotalNumberOfSeries();
    }
    
	public boolean isChecked() {
		return this.checked;
	}
	
	public void setChecked(boolean b) {
		this.checked = b;
	}
		
	private boolean checked;
	
	private PatientSearchResult patient;


}