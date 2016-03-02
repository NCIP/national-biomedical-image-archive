/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;


public class StudyNumberDTO  {

	public StudyNumberDTO(int patientPkId, 
			              String patientId, 
			              String project, 
			              int studyNumber, 
			              int seriesNumber) {
		this.patientPkId = patientPkId;
		this.patientId = patientId;
		this.studyNumber = studyNumber;
		this.seriesNumber = seriesNumber;	
		this.project = project;
	}

	/**
	* Retreives the value of patientId attribute
	* @return patientId
	**/

	public String getPatientId(){
		return patientId;
	}


	
	/**
	* Retreives the value of project attribute
	* @return project
	**/

	public String getProject(){
		return project;
	}

	
	
	/**
	* Retreives the value of seriesNumber attribute
	* @return seriesNumber
	**/

	public Integer getSeriesNumber(){
		return seriesNumber;
	}

	

	/**
	* Retreives the value of studyNumber attribute
	* @return studyNumber
	**/

	public Integer getStudyNumber(){
		return studyNumber;
	}

	

	public Integer getPatientPkId(){
		return patientPkId;
	}

	////////////////////////////////////////////////PRIVATE/////////////////////////////////////////
	
	/**
	* 	**/
	private Integer seriesNumber;
	/**
	* 	**/
	private Integer studyNumber;
	/**
	* 	**/
	private Integer patientPkId;	
	
    
	
	/**
* 	**/
	private String patientId;
	
	
	/**
	 * 	**/
	private String project;	
}