/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

public class DeletionAuditPatientInfo {
	
	private String patientId;
	private Integer totalImage;
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public Integer getTotalImage() {
		return totalImage;
	}
	public void setTotalImage(Integer totalImage) {
		this.totalImage = totalImage;
	}
	
	

}
