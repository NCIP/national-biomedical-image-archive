/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import gov.nih.nci.ncia.dto.DicomTagDTO;

import java.util.Collection;
import java.util.List;
import java.io.Serializable;
	/**
	* Defines the characteristics of a patient who is the subject of one or more medical studies that produce medical images.	**/
public class PatientDocument  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* the patient's/participant's self declared ethnic origination, independent of racial origination, based on OMB approved categories.	**/
	private String ethnicGroup;
	/**
	* Retreives the value of ethnicGroup attribute
	* @return ethnicGroup
	**/

	public String getEthnicGroup(){
		return ethnicGroup;
	}

	/**
	* Sets the value of ethnicGroup attribue
	**/

	public void setEthnicGroup(String ethnicGroup){
		this.ethnicGroup = ethnicGroup;
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
	* the month, day and year on which the patient/participant was born.	**/
	private java.util.Date patientBirthDate;
	/**
	* Retreives the value of patientBirthDate attribute
	* @return patientBirthDate
	**/

	public java.util.Date getPatientBirthDate(){
		return patientBirthDate;
	}

	/**
	* Sets the value of patientBirthDate attribue
	**/

	public void setPatientBirthDate(java.util.Date patientBirthDate){
		this.patientBirthDate = patientBirthDate;
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
	* the current legal name of the person registered on the clinical trial.	**/
	private String patientName;
	/**
	* Retreives the value of patientName attribute
	* @return patientName
	**/

	public String getPatientName(){
		return patientName;
	}

	/**
	* Sets the value of patientName attribue
	**/

	public void setPatientName(String patientName){
		this.patientName = patientName;
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
	* Identifier assigned to a clinical trial subject.	**/
	private String trialSubjectId;
	/**
	* Retreives the value of trialSubjectId attribute
	* @return trialSubjectId
	**/

	public String getTrialSubjectId(){
		return trialSubjectId;
	}

	/**
	* Sets the value of trialSubjectId attribue
	**/

	public void setTrialSubjectId(String trialSubjectId){
		this.trialSubjectId = trialSubjectId;
	}
	
		/**
	* One or more characters used to identify the subject of blinded evaluations.	**/
	private String trialSubjectReadingId;
	/**
	* Retreives the value of trialSubjectReadingId attribute
	* @return trialSubjectReadingId
	**/

	public String getTrialSubjectReadingId(){
		return trialSubjectReadingId;
	}

	/**
	* Sets the value of trialSubjectReadingId attribue
	**/

	public void setTrialSubjectReadingId(String trialSubjectReadingId){
		this.trialSubjectReadingId = trialSubjectReadingId;
	}
	

	/**
	* An associated gov.nih.nci.ncia.domain.TrialSite object
	**/
			
	private TrialSiteDoc trialSite;
	/**
	* Retreives the value of trialSite attribue
	* @return trialSite
	**/
	
	public TrialSiteDoc getTrialSite(){
		return trialSite;
	}
	/**
	* Sets the value of trialSite attribue
	**/

	public void setTrialSite(TrialSiteDoc trialSite){
		this.trialSite = trialSite;
	}
			
	/**
	* An associated gov.nih.nci.ncia.domain.Study object's collection 
	**/
			
	private Collection<StudyDoc> studyCollection;
	/**
	* Retreives the value of studyCollection attribue
	* @return studyCollection
	**/

	public Collection<StudyDoc> getStudyCollection(){
		return studyCollection;
	}

	/**
	* Sets the value of studyCollection attribue
	**/

	public void setStudyCollection(Collection<StudyDoc> studyCollection){
		this.studyCollection = studyCollection;
	}
		
	/**
	* An associated gov.nih.nci.ncia.domain.TrialDataProvenance object
	**/
			
	private TrialDataProvenanceDoc dataProvenance;
	/**
	* Retreives the value of dataProvenance attribue
	* @return dataProvenance
	**/
	
	public TrialDataProvenanceDoc getDataProvenance(){
		return dataProvenance;
	}
	/**
	* Sets the value of dataProvenance attribue
	**/

	public void setDataProvenance(TrialDataProvenanceDoc dataProvenance){
		this.dataProvenance = dataProvenance;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof PatientDocument) 
		{
			PatientDocument c =(PatientDocument)obj; 			 
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

	@Override
	public String toString() {
		return "PatientDocument [ethnicGroup=" + ethnicGroup
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + "]";
	}
	
}