/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.io.Serializable;
/**
 * Represents a series for data transfer purposes
 *
 * @author lethai
 */
public class CustomSeriesDTO implements  Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    
    private String seriesUID;
    private Integer numberImages;
    private String modality;
    private String manufacturer;
    private Integer id;
    private boolean annotationsFlag;
    private Long annotationsSize = 0L;
    private String patientId;
    private String studyUid;
    private Integer studyPkId;
    private String project;
    private String description;
    private String securityGroup;
    
	public String getSeriesUID() {
		return seriesUID;
	}
	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}
	public Integer getNumberImages() {
		return numberImages;
	}
	public void setNumberImages(Integer numberImages) {
		this.numberImages = numberImages;
	}
	public String getModality() {
		return modality;
	}
	public void setModality(String modality) {
		this.modality = modality;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isAnnotationsFlag() {		
		return annotationsFlag;
	}
	public void setAnnotationsFlag(boolean annotationsFlag) {
		this.annotationsFlag = annotationsFlag;
	}
	public Long getAnnotationsSize() {
		return annotationsSize;
	}
	public void setAnnotationsSize(Long annotationsSize) {
		this.annotationsSize = annotationsSize;
	}

	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getStudyUid() {
		return studyUid;
	}
	public void setStudyUid(String studyUid) {
		this.studyUid = studyUid;
	}
	public Integer getStudyPkId() {
		return studyPkId;
	}
	public void setStudyPkId(Integer studyPkId) {
		this.studyPkId = studyPkId;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSecurityGroup() {
		return securityGroup;
	}
	public void setSecurityGroup(String securityGroup) {
		this.securityGroup = securityGroup;
	}
}

