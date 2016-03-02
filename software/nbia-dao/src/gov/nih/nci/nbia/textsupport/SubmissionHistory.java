/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import java.io.Serializable;
	/**
	* 	**/
public class SubmissionHistory  implements Serializable
{
	public static final int NEW_IMAGE_SUBMISSION_OPERATION = 0;
	public static final int REPLACE_IMAGE_SUBMISSION_OPERATION = 1;
	public static final int ANNOTATION_SUBMISSION_OPERATION = 2;

	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	/**
     *
     **/
	protected Long id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Long id){
		this.id = id;
	}

	/**
	* The unique number assigned to identify a patient/participant on a protocol.	**/
	private String patientId;
	/**
	* Retreives the value of patientId attribute
	* @return patientId
	**/

	public String getPatientId(){
		return patientId;
	}

	/**
	* Sets the value of patientId attribue
	**/

	public void setPatientId(String patientId){
		this.patientId = patientId;
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
	* Unique identifer for a Service-Object Pair (SOP) instance, as specified in a DICOM tag.	**/
	private String SOPInstanceUID;
	/**
	* Retreives the value of SOPInstanceUID attribute
	* @return SOPInstanceUID
	**/

	public String getSOPInstanceUID(){
		return SOPInstanceUID;
	}

	/**
	* Sets the value of SOPInstanceUID attribue
	**/

	public void setSOPInstanceUID(String SOPInstanceUID){
		this.SOPInstanceUID = SOPInstanceUID;
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
	* The date on which the image was submitted.	**/
	private java.util.Date submissionDate;
	/**
	* Retreives the value of submissionDate attribute
	* @return submissionDate
	**/

	public java.util.Date getSubmissionDate(){
		return submissionDate;
	}

	/**
	* Sets the value of submissionDate attribue
	**/

	public void setSubmissionDate(java.util.Date submissionDate){
		this.submissionDate = submissionDate;
	}
	
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
/*
	private boolean newPatient;
	private boolean newStudy;
	private boolean newSeries;
	
	public boolean isNewPatient() {
		return newPatient;
	}

	public void setNewPatient(boolean newPatient) {
		this.newPatient = newPatient;
	}

	public boolean isNewStudy() {
		return newStudy;
	}

	public void setNewStudy(boolean newStudy) {
		this.newStudy = newStudy;
	}

	public boolean isNewSeries() {
		return newSeries;
	}

	public void setNewSeries(boolean newSeries) {
		this.newSeries = newSeries;
	}
	*/
	private int operationType;
	public int getOperationType() {
		return this.operationType;
	}
	
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof SubmissionHistory)
		{
			SubmissionHistory c =(SubmissionHistory)obj;
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

}