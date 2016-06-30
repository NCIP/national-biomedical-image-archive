/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;


import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nih.nci.nbia.qctool.VisibilityStatus;
import gov.nih.nci.nbia.searchresult.APIURLHolder;

public class QcSearchResultDTO {
	private String collection;
	private String site;
	private String patientId;
	private String study;
	private	String series;
	private Date creationDate;
	private String visibility;
	private boolean selected;
	private String modality;
	private String seriesDescription;
	private String user;
	
	private String batch;
	private String submissionType;
	private String releasedStatus;
	private String trialDpPkId;
	
	
	public QcSearchResultDTO(String collection, 
							 String site,
							 String patientId,
							 String study,
							 String series,
							 Date creationDate,
							 String visibility,
							 String modality, 
							 String seriesDesc, 
							 
							 String batch, 
							 String submissionType, String releasedStatus, String trialDpPkId) {
		setCollection(collection);
		setSite(site);
		setPatientId(patientId);
		setStudy(study);
		setSeries(series);
		setCreationDate(creationDate);
		setVisibility(visibility);
		selected = false;
		setModality(modality);
		setSeriesDescription(seriesDesc);
		
		setBatch(batch);
		setSubmissionType(submissionType);
		setReleasedStatus(releasedStatus);
		setTrialDpPkId(trialDpPkId);		
		
	}
	
	public QcSearchResultDTO(QcSearchResultDTO obj) {
		setCollection(obj.collection);
		setSite(obj.site);
		setPatientId(obj.patientId);
		setStudy(obj.study);
		setSeries(obj.series);
		setCreationDate(obj.creationDate);
		setVisibility(obj.visibility);
		selected = obj.selected;
		setModality(obj.modality);
		setSeriesDescription(obj.seriesDescription);
		
		setBatch(obj.batch);
		setSubmissionType(obj.submissionType);		
		setReleasedStatus(obj.releasedStatus);	
		setTrialDpPkId(obj.trialDpPkId);
		
}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection){
		this.collection = collection;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getCollectionSite(){
		return collection+"//"+site;
	}
	
	
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	
	public String getVisibilityStatus() {
		return VisibilityStatus.statusFactory(Integer.parseInt(visibility)).getText();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getDateTime() {
		if (creationDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			return sdf.format(creationDate);
		} else {
			return "N/A";
		}
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getSeriesDescription() {
		return seriesDescription;
	}

	public void setSeriesDescription(String seriesDescription) {
		this.seriesDescription = seriesDescription;
	}
	
	//////////////////////////////////////////////////
	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch; 
	
	}
	//--------------------------------
	
	public String getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}
	
	//--------------------------------
	
		public String getReleasedStatus() {
			return releasedStatus;
		}

		public void setReleasedStatus(String releasedStatus) {
			this.releasedStatus = releasedStatus;
		}
	//--------------------------------
	
	public String getTrialDpPkId() {
		return trialDpPkId;
	}

    public void setTrialDpPkId(String trialDpPkId) {
	    this.trialDpPkId = trialDpPkId;
    }
	
	////////////////////////////////////////////////////////

	public String getLink() {
		return APIURLHolder.getUrl()+"/oviyam2/oviyam?serverName="+APIURLHolder.getUrl()+
		"/nbia-api/services/o&studyUID="+study+"&seriesUid="+series+"&oviyamId="+APIURLHolder.addUser(user)+
		"&wadoUrl="+APIURLHolder.getWadoUrl();
	}

	public void setLink(String link) {
		//this.link = link;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	

//	public VisibilityStatus getVisibilityStatus() {
//		return visibilityStatus;
//	}
//
//	public void setVisibilityStatus(VisibilityStatus visibilityStatus) {
//		this.visibilityStatus = visibilityStatus;
//	}
	
}
