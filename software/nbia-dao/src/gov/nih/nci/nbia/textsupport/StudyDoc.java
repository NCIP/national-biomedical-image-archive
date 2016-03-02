/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import java.util.Collection;
import java.io.Serializable;
	/**
	* Defines the characteristics of a medical study performed on a patient. A study is a collection of one or more series of medical images, presentation states, and/or SR documents that are logically related for the purpose of diagnosing a patient.	**/
public class StudyDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


		/**
	* Abbreviated text field to describe a person's relevant medical history.	**/
	private String additionalPatientHistory;
	/**
	* Retreives the value of additionalPatientHistory attribute
	* @return additionalPatientHistory
	**/

	public String getAdditionalPatientHistory(){
		return additionalPatientHistory;
	}

	/**
	* Sets the value of additionalPatientHistory attribue
	**/

	public void setAdditionalPatientHistory(String additionalPatientHistory){
		this.additionalPatientHistory = additionalPatientHistory;
	}

		/**
	* Coded sequence that conveys the admitting diagnosis (diagnoses).	**/
	private String admittingDiagnosesCodeSeq;
	/**
	* Retreives the value of admittingDiagnosesCodeSeq attribute
	* @return admittingDiagnosesCodeSeq
	**/

	public String getAdmittingDiagnosesCodeSeq(){
		return admittingDiagnosesCodeSeq;
	}

	/**
	* Sets the value of admittingDiagnosesCodeSeq attribue
	**/

	public void setAdmittingDiagnosesCodeSeq(String admittingDiagnosesCodeSeq){
		this.admittingDiagnosesCodeSeq = admittingDiagnosesCodeSeq;
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
	* Name of the primary occupation of a person associated with a clinical trial.	**/
	private String occupation;
	/**
	* Retreives the value of occupation attribute
	* @return occupation
	**/

	public String getOccupation(){
		return occupation;
	}

	/**
	* Sets the value of occupation attribue
	**/

	public void setOccupation(String occupation){
		this.occupation = occupation;
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
	* Numeric value to capture the length or size of a person in meters.	**/
	private Double patientSize;
	/**
	* Retreives the value of patientSize attribute
	* @return patientSize
	**/

	public Double getPatientSize(){
		return patientSize;
	}

	/**
	* Sets the value of patientSize attribue
	**/

	public void setPatientSize(Double patientSize){
		this.patientSize = patientSize;
	}

		/**
	* Weight	**/
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
	* User or equipment generated Study Identifier.	**/
	private String studyId;
	/**
	* Retreives the value of studyId attribute
	* @return studyId
	**/

	public String getStudyId(){
		return studyId;
	}

	/**
	* Sets the value of studyId attribue
	**/

	public void setStudyId(String studyId){
		this.studyId = studyId;
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
	* Time in HR(24):Mn an imaging study started.	**/
	private String studyTime;
	/**
	* Retreives the value of studyTime attribute
	* @return studyTime
	**/

	public String getStudyTime(){
		return studyTime;
	}

	/**
	* Sets the value of studyTime attribue
	**/

	public void setStudyTime(String studyTime){
		this.studyTime = studyTime;
	}

		/**
	* Text to describe the grouping together of one or more studies as a clinical time point or submission in a clinical trial.	**/
	private String timePointDesc;
	/**
	* Retreives the value of timePointDesc attribute
	* @return timePointDesc
	**/

	public String getTimePointDesc(){
		return timePointDesc;
	}

	/**
	* Sets the value of timePointDesc attribue
	**/

	public void setTimePointDesc(String timePointDesc){
		this.timePointDesc = timePointDesc;
	}

		/**
	* Identifier specifying one or more studies that are grouped together as a single time point or submission in a clinical trial.	**/
	private String timePointId;
	/**
	* Retreives the value of timePointId attribute
	* @return timePointId
	**/

	public String getTimePointId(){
		return timePointId;
	}

	/**
	* Sets the value of timePointId attribue
	**/

	public void setTimePointId(String timePointId){
		this.timePointId = timePointId;
	}


	/**
	* An associated gov.nih.nci.ncia.domain.GeneralSeries object's collection
	**/

	private Collection<GeneralSeriesSubDoc> generalSeriesCollection;
	/**
	* Retreives the value of generalSeriesCollection attribue
	* @return generalSeriesCollection
	**/

	public Collection<GeneralSeriesSubDoc> getGeneralSeriesCollection(){
		return generalSeriesCollection;
	}

	/**
	* Sets the value of generalSeriesCollection attribue
	**/

	public void setGeneralSeriesCollection(Collection<GeneralSeriesSubDoc> generalSeriesCollection){
		this.generalSeriesCollection = generalSeriesCollection;
	}


	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof StudyDoc)
		{
			StudyDoc c =(StudyDoc)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

}