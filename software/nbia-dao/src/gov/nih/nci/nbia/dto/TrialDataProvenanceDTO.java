/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

public class TrialDataProvenanceDTO   {

    public TrialDataProvenanceDTO(String project, String site) {
    	this.project = project;
    	this.site = site;
    }


    public String getProject() {
    	return this.project;
    }


	public String getSite() {
		return site;
	}
	
	
	///////////////////////////////////////PRIVATE//////////////////////////////////
    private String project;
    private String site;	
}