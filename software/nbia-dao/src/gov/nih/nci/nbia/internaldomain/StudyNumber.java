/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;


import java.io.Serializable;
	/**
	* 	**/
public class StudyNumber  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* 	**/
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
	* 	**/
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
	* 	**/
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
	* 	**/
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
	* 	**/
	private Integer studyNumber;
	/**
	* Retreives the value of studyNumber attribute
	* @return studyNumber
	**/

	public Integer getStudyNumber(){
		return studyNumber;
	}

	/**
	* Sets the value of studyNumber attribue
	**/

	public void setStudyNumber(Integer studyNumber){
		this.studyNumber = studyNumber;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof StudyNumber) 
		{
			StudyNumber c =(StudyNumber)obj; 			 
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