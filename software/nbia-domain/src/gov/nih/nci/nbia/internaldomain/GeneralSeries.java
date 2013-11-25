/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
	/**
	* A set of images.  Typically, a series is made up of all of the slice generated from a scan.  Each series is associated with exactly one Study.	**/
public class GeneralSeries  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	private Long totalSize;
	private Integer imageCount;
	private Long annotationTotalSize;

	private Date maxSubmissionTimestamp;
	
	/**
	* An name chosen by the image submitter to identify the collection that the image is a part of.	**/
	private String site;
	/**
	* Retreives the value of project attribute
	* @return project
	**/

	public String getSite(){
		return site;
	}

	/**
	* Sets the value of project attribue
	**/

	public void setSite(String site){
		this.site = site;
	}
	
	/**
	* An name chosen by the image submitter to identify the collection that the image is a part of.	**/
	private String project;
	/**
	* Retreives the value of project attribute
	* @return project
	**/

	public String getProject(){
		return project;
	}

	/**
	* Sets the value of project attribue
	**/

	public void setProject(String project){
		this.project = project;
	}	

	
	/**
	* Unique identifier for an occurrence of a medical imaging study.	**/
	private String studyInstanceUID;
	/**
	* Retreives the value of studyInstanceUID attribute
	* @return studyInstanceUID
	**/

	public String getStudyInstanceUID(){
		return studyInstanceUID;
	}

	/**
	* Sets the value of studyInstanceUID attribue
	**/

	public void setStudyInstanceUID(String studyInstanceUID){
		this.studyInstanceUID = studyInstanceUID;
	}
	
		/**
	* Text to capture a description of the admitting diagnosis (diagnoses) of a patient/participant.	**/
	private String admittingDiagnosesDesc;
	/**
	* Retreives the value of admittingDiagnosesDesc attribute
	* @return admittingDiagnosesDesc
	**/

	public String getAdmittingDiagnosesDesc(){
		return admittingDiagnosesDesc;
	}

	/**
	* Sets the value of admittingDiagnosesDesc attribue
	**/

	public void setAdmittingDiagnosesDesc(String admittingDiagnosesDesc){
		this.admittingDiagnosesDesc = admittingDiagnosesDesc;
	}

		/**
	* Age at the time of study enrollment, expressed in number of years completed at the last birthday.	**/
	private String ageGroup;
	/**
	* Retreives the value of ageGroup attribute
	* @return ageGroup
	**/

	public String getAgeGroup(){
		return ageGroup;
	}

	/**
	* Sets the value of ageGroup attribue
	**/

	public void setAgeGroup(String ageGroup){
		this.ageGroup = ageGroup;
	}

		/**
	* Annotated	**/
	private Boolean annotationsFlag;
	/**
	* Retreives the value of annotationsFlag attribute
	* @return annotationsFlag
	**/

	public Boolean getAnnotationsFlag(){
		return annotationsFlag;
	}

	/**
	* Sets the value of annotationsFlag attribue
	**/

	public void setAnnotationsFlag(Boolean annotationsFlag){
		this.annotationsFlag = annotationsFlag;
	}

		/**
	* Text description of the anatomic part of the body examined.	**/
	private String bodyPartExamined;
	/**
	* Retreives the value of bodyPartExamined attribute
	* @return bodyPartExamined
	**/

	public String getBodyPartExamined(){
		return bodyPartExamined;
	}

	/**
	* Sets the value of bodyPartExamined attribue
	**/

	public void setBodyPartExamined(String bodyPartExamined){
		this.bodyPartExamined = bodyPartExamined;
	}

		/**
	* Unique identifier for the frame of reference for an imaging series. Each series shall have a single Frame of Reference UID, while multiple Series within a Study may share a Frame of Reference UID.	**/
	private String frameOfReferenceUID;
	/**
	* Retreives the value of frameOfReferenceUID attribute
	* @return frameOfReferenceUID
	**/

	public String getFrameOfReferenceUID(){
		return frameOfReferenceUID;
	}

	/**
	* Sets the value of frameOfReferenceUID attribue
	**/

	public void setFrameOfReferenceUID(String frameOfReferenceUID){
		this.frameOfReferenceUID = frameOfReferenceUID;
	}

		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
	private Integer id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Integer id){
		this.id = id;
	}

		/**
	* Position or laterality of (paired) body part examined.	**/
	private String laterality;
	/**
	* Retreives the value of laterality attribute
	* @return laterality
	**/

	public String getLaterality(){
		return laterality;
	}

	/**
	* Sets the value of laterality attribue
	**/

	public void setLaterality(String laterality){
		this.laterality = laterality;
	}

		/**
	* Type of equipment that originally acquired the data used to create the images in this series.	**/
	private String modality;
	/**
	* Retreives the value of modality attribute
	* @return modality
	**/

	public String getModality(){
		return modality;
	}

	/**
	* Sets the value of modality attribue
	**/

	public void setModality(String modality){
		this.modality = modality;
	}

		/**
	* Age at the time of study enrollment, expressed in number of years completed at the last birthday.	**/
	private String patientAge;
	/**
	* Retreives the value of patientAge attribute
	* @return patientAge
	**/

	public String getPatientAge(){
		return patientAge;
	}

	/**
	* Sets the value of patientAge attribue
	**/

	public void setPatientAge(String patientAge){
		this.patientAge = patientAge;
	}

		/**
	* The unique number assigned to identify a patient/participant on a protocol.	**/
	private Integer patientPkId;
	/**
	* Retreives the value of patientId attribute
	* @return patientId
	**/

	public Integer getPatientPkId(){
		return patientPkId;
	}

	/**
	* Sets the value of patientId attribue
	**/

	public void setPatientPkId(Integer patientPkId){
		this.patientPkId = patientPkId;
	}


	private String patientId;

	public String getPatientId()
	{
		return patientId;
	}

	public void setPatientId(String patientId)
	{
		this.patientId = patientId;
	}
		/**
	* Text code to represent a patient's sex determination.	**/
	private String patientSex;
	/**
	* Retreives the value of patientSex attribute
	* @return patientSex
	**/

	public String getPatientSex(){
		return patientSex;
	}

	/**
	* Sets the value of patientSex attribue
	**/

	public void setPatientSex(String patientSex){
		this.patientSex = patientSex;
	}

		/**
	* Measurement of an individual's weight in kilograms.	**/
	private Double patientWeight;
	/**
	* Retreives the value of patientWeight attribute
	* @return patientWeight
	**/

	public Double getPatientWeight(){
		return patientWeight;
	}

	/**
	* Sets the value of patientWeight attribue
	**/

	public void setPatientWeight(Double patientWeight){
		this.patientWeight = patientWeight;
	}

		/**
	* User-defined description of the conditions under which the (imaging) Series was performed.	**/
	private String protocolName;
	/**
	* Retreives the value of protocolName attribute
	* @return protocolName
	**/

	public String getProtocolName(){
		return protocolName;
	}

	/**
	* Sets the value of protocolName attribue
	**/

	public void setProtocolName(String protocolName){
		this.protocolName = protocolName;
	}

		/**
	* The security group to which the series belongs as determined by the image QA analyst.   Series security groups are used to impose security restrictions on a group of series.	**/
	private String securityGroup;
	/**
	* Retreives the value of securityGroup attribute
	* @return securityGroup
	**/

	public String getSecurityGroup(){
		return securityGroup;
	}

	/**
	* Sets the value of securityGroup attribue
	**/

	public void setSecurityGroup(String securityGroup){
		this.securityGroup = securityGroup;
	}

		/**
	* Date the Series started.	**/
	private java.util.Date seriesDate;
	/**
	* Retreives the value of seriesDate attribute
	* @return seriesDate
	**/

	public java.util.Date getSeriesDate(){
		return seriesDate;
	}

	/**
	* Sets the value of seriesDate attribue
	**/

	public void setSeriesDate(java.util.Date seriesDate){
		this.seriesDate = seriesDate;
	}

		/**
	* User-provided text description of the imaging series.	**/
	private String seriesDesc;
	/**
	* Retreives the value of seriesDesc attribute
	* @return seriesDesc
	**/

	public String getSeriesDesc(){
		return seriesDesc;
	}

	/**
	* Sets the value of seriesDesc attribue
	**/

	public void setSeriesDesc(String seriesDesc){
		this.seriesDesc = seriesDesc;
	}

		/**
	* Unique identifer of a study series.	**/
	private String seriesInstanceUID;
	/**
	* Retreives the value of seriesInstanceUID attribute
	* @return seriesInstanceUID
	**/

	public String getSeriesInstanceUID(){
		return seriesInstanceUID;
	}

	/**
	* Sets the value of seriesInstanceUID attribue
	**/

	public void setSeriesInstanceUID(String seriesInstanceUID){
		this.seriesInstanceUID = seriesInstanceUID;
	}

		/**
	* A number that identifies this Series.	**/
	private Integer seriesNumber;
	/**
	* Retreives the value of seriesNumber attribute
	* @return seriesNumber
	**/

	public Integer getSeriesNumber(){
		return seriesNumber;
	}

	/**
	* Sets the value of seriesNumber attribue
	**/

	public void setSeriesNumber(Integer seriesNumber){
		this.seriesNumber = seriesNumber;
	}

		/**
	* Date an imaging study started.	**/
	private java.util.Date studyDate;
	/**
	* Retreives the value of studyDate attribute
	* @return studyDate
	**/

	public java.util.Date getStudyDate(){
		return studyDate;
	}

	/**
	* Sets the value of studyDate attribue
	**/

	public void setStudyDate(java.util.Date studyDate){
		this.studyDate = studyDate;
	}

		/**
	* Institution-generated description or classification of the study (component) performed.	**/
	private String studyDesc;
	/**
	* Retreives the value of studyDesc attribute
	* @return studyDesc
	**/

	public String getStudyDesc(){
		return studyDesc;
	}

	/**
	* Sets the value of studyDesc attribue
	**/

	public void setStudyDesc(String studyDesc){
		this.studyDesc = studyDesc;
	}

		/**
	* UID of common synchronization environment.  A set of equipment may share a common acquisition synchronization environment.	**/
	private String syncFrameOfRefUID;
	/**
	* Retreives the value of syncFrameOfRefUID attribute
	* @return syncFrameOfRefUID
	**/

	public String getSyncFrameOfRefUID(){
		return syncFrameOfRefUID;
	}

	/**
	* Sets the value of syncFrameOfRefUID attribue
	**/

	public void setSyncFrameOfRefUID(String syncFrameOfRefUID){
		this.syncFrameOfRefUID = syncFrameOfRefUID;
	}

		/**
	* The numeric or alphanumeric identification assigned to the study by the NCI. Inter-Group protocols should use the lead Groups protocol number.	**/
	private String trialProtocolId;
	/**
	* Retreives the value of trialProtocolId attribute
	* @return trialProtocolId
	**/

	public String getTrialProtocolId(){
		return trialProtocolId;
	}

	/**
	* Sets the value of trialProtocolId attribue
	**/

	public void setTrialProtocolId(String trialProtocolId){
		this.trialProtocolId = trialProtocolId;
	}

		/**
	* Descriptive text used to represent the long title or name of a protocol.	**/
	private String trialProtocolName;
	/**
	* Retreives the value of trialProtocolName attribute
	* @return trialProtocolName
	**/

	public String getTrialProtocolName(){
		return trialProtocolName;
	}

	/**
	* Sets the value of trialProtocolName attribue
	**/

	public void setTrialProtocolName(String trialProtocolName){
		this.trialProtocolName = trialProtocolName;
	}

		/**
	* Text name to capture the name of the site responsible for submitting clinical trial data.	**/
	private String trialSiteName;
	/**
	* Retreives the value of trialSiteName attribute
	* @return trialSiteName
	**/

	public String getTrialSiteName(){
		return trialSiteName;
	}

	/**
	* Sets the value of trialSiteName attribue
	**/

	public void setTrialSiteName(String trialSiteName){
		this.trialSiteName = trialSiteName;
	}

		/**
	* Indicates the QA status of a series	**/
	private String visibility;
	/**
	* Retreives the value of visibility attribute
	* @return visibility
	**/

	public String getVisibility(){
		return visibility;
	}

	/**
	* Sets the value of visibility attribue
	**/

	public void setVisibility(String visibility){
		this.visibility = visibility;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.Study object
	**/

	private Study study;
	/**
	* Retreives the value of study attribue
	* @return study
	**/

	public Study getStudy(){
		return study;
	}
	/**
	* Sets the value of study attribue
	**/

	public void setStudy(Study study){
		this.study = study;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.GeneralImage object's collection
	**/

	private Collection<GeneralImage> generalImageCollection;
	/**
	* Retreives the value of generalImageCollection attribue
	* @return generalImageCollection
	**/

	public Collection<GeneralImage> getGeneralImageCollection(){
		return generalImageCollection;
	}

	/**
	* Sets the value of generalImageCollection attribue
	**/

	public void setGeneralImageCollection(Collection<GeneralImage> generalImageCollection){
		this.generalImageCollection = generalImageCollection;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.GeneralEquipment object
	**/

	private GeneralEquipment generalEquipment;
	/**
	* Retreives the value of generalEquipment attribue
	* @return generalEquipment
	**/

	public GeneralEquipment getGeneralEquipment(){
		return generalEquipment;
	}
	/**
	* Sets the value of generalEquipment attribue
	**/

	public void setGeneralEquipment(GeneralEquipment generalEquipment){
		this.generalEquipment = generalEquipment;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.Annotation object's collection
	**/

	private Collection<Annotation> annotationCollection;
	/**
	* Retreives the value of annotationCollection attribue
	* @return annotationCollection
	**/

	public Collection<Annotation> getAnnotationCollection(){
		return annotationCollection;
	}

	/**
	* Sets the value of annotationCollection attribue
	**/

	public void setAnnotationCollection(Collection<Annotation> annotationCollection){
		this.annotationCollection = annotationCollection;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof GeneralSeries)
		{
			GeneralSeries c =(GeneralSeries)obj;
			if(getId() != null && getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null) {
			return getId().hashCode();
		}
		return 0;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public Long getAnnotationTotalSize() {
		return annotationTotalSize;
	}

	public void setAnnotationTotalSize(Long annotationTotalSize) {
		this.annotationTotalSize = annotationTotalSize;
	}

	public Date getMaxSubmissionTimestamp() {
		return maxSubmissionTimestamp;
	}

	public void setMaxSubmissionTimestamp(Date maxSubmissionTimestamp) {
		this.maxSubmissionTimestamp = maxSubmissionTimestamp;
	}
	
	String maxFrameCount;
	public String getMaxFrameCount() {
		return maxFrameCount;
	}

	public void setMaxFrameCount(String maxFrameCount) {
		this.maxFrameCount = maxFrameCount;
	}
	
}
