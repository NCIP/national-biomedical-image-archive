/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.ncia.gridzip;

/**
 * @author lethai
 *
 */
public class ZippingDTO {
	private String project;
	private String siteName;
	private String patientId;
	private String studyInstanceUid;
	private String seriesInstanceUid;
	private String sopInstanceUidAsFileName;
	private String filePath;
	public String getSopInstanceUidAsFileName() {
		return sopInstanceUidAsFileName;
	}
	public void setSopInstanceUidAsFileName(String sopInstanceUidAsFileName) {
		this.sopInstanceUidAsFileName = sopInstanceUidAsFileName;
	}
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
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getStudyInstanceUid() {
		return studyInstanceUid;
	}
	public void setStudyInstanceUid(String studyInstanceUid) {
		this.studyInstanceUid = studyInstanceUid;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
