/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.download;

public class SeriesData {
	
	private String collection;
	private String patientId;
	private String studyInstanceUid;
	private String seriesInstanceUid;
	private boolean hasAnnotation;
	private String numberImages;
	private Integer imagesSize;
	private Integer annoSize;
	private String url;
	private String displayName;
	private boolean local;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getImagesSize() {
		return imagesSize;
	}
	public void setImagesSize(Integer imagesSize) {
		this.imagesSize = imagesSize;
	}
	public Integer getAnnoSize() {
		return annoSize;
	}
	public void setAnnoSize(Integer annoSize) {
		this.annoSize = annoSize;
	}
	public String getNumberImages() {
		return numberImages;
	}
	public void setNumberImages(String numberImages) {
		this.numberImages = numberImages;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
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
	public boolean isHasAnnotation() {
		return hasAnnotation;
	}
	public void setHasAnnotation(boolean hasAnnotation) {
		this.hasAnnotation = hasAnnotation;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	

}
