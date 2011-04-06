package gov.nih.nci.nbia.dto;


import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nih.nci.nbia.qctool.VisibilityStatus;

public class QcSearchResultDTO {
	private String collection;
	private String site;
	private String patientId;
	private String study;
	private	String series;
	private Date creationDate;
	private String visibility;
	private boolean selected;
	
	public QcSearchResultDTO(String collection, 
							 String site,
							 String patientId,
							 String study,
							 String series,
							 Date creationDate,
							 String visibility) {
		setCollection(collection);
		setSite(site);
		setPatientId(patientId);
		setStudy(study);
		setSeries(series);
		setCreationDate(creationDate);
		setVisibility(visibility);
		selected = false;
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
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
		return sdf.format(creationDate);
	}
	

//	public VisibilityStatus getVisibilityStatus() {
//		return visibilityStatus;
//	}
//
//	public void setVisibilityStatus(VisibilityStatus visibilityStatus) {
//		this.visibilityStatus = visibilityStatus;
//	}
}
