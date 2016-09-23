/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchresults;

import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResult;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResultImpl;

public class PatientResultWrapper {
	public PatientResultWrapper(PatientSearchResult patient) {
		this.patient = patient;
	}
	
	public PatientSearchResult getPatient() {
		return patient;
	}

	
	public String getBasketKey() {
		return patient.getId().toString();
	}
	
    
    /**
     * Get the study counts for display. This includes the filtered number and
     * the total number.
     */ 
    public String getStudyCounts() {
        return patient.computeFilteredNumberOfStudies().toString() ;
    }

    /**
     * Get the study counts for display. This includes the filtered number and
     * the total number.
     */ 
    public String getSeriesCounts() {
        return patient.computeFilteredNumberOfSeries().toString();
    }
    
	public boolean isChecked() {
		return this.checked;
	}
	
	public void setChecked(boolean b) {
		this.checked = b;
	}
		
	private boolean checked;
	
	private PatientSearchResult patient;
    
	public String getHit()
	{
		if (patient instanceof PatientTextSearchResultImpl)
		{
		   return ((PatientTextSearchResult)patient).getHit();
		}  else
		{
		   return "";
		}
	}
	

}