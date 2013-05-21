package gov.nih.nci.nbia.dto;

/**
 * This class holds the part of patient information from Patient object.
 * @author zhoujim
 *
 */
public class PatientDTO {
	//It might be necessary to add more fields. Current, project and site name have been
	//used in this object.
	private String project;
	private String siteName;
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	

}
