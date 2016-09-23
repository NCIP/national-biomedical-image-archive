/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

public class DeletionAuditStudyInfo {
	
	private String studyInstanceUID;
	private Integer totalImage;
	
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}
	public Integer getTotalImage() {
		return totalImage;
	}
	public void setTotalImage(Integer totalImage) {
		this.totalImage = totalImage;
	}
	

}
