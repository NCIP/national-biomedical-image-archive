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
