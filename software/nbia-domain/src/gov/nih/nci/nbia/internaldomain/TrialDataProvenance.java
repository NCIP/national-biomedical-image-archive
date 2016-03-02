/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.util.Collection;
import java.io.Serializable;
	/**
	* Data (chosen by the image submitter) to describe the clinical trial and site from which the image and related data originated.	**/
public class TrialDataProvenance  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


		/**
	* A numerical identifier chosen by the image submitter to identify the clinical trial site from which the image originated.	**/
	private String dpSiteId;
	/**
	* Retrieves the value of dpSiteId attribute
	* @return dpSiteId
	**/

	public String getDpSiteId(){
		return dpSiteId;
	}

	/**
	* Sets the value of dpSiteId attribute
	**/

	public void setDpSiteId(String dpSiteId){
		this.dpSiteId = dpSiteId;
	}

		/**
	* A name chosen by the image submitter to identify the clinical trial site from which the image originated.	**/
	private String dpSiteName;
	/**
	* Retrieves the value of dpSiteName attribute
	* @return dpSiteName
	**/

	public String getDpSiteName(){
		return dpSiteName;
	}

	/**
	* Sets the value of dpSiteName attribute
	**/

	public void setDpSiteName(String dpSiteName){
		this.dpSiteName = dpSiteName;
	}

		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
	private Integer id;
	/**
	* Retrieves the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Integer id){
		this.id = id;
	}

		/**
	* An name chosen by the image submitter to identify the collection that the image is a part of.	**/
	private String project;
	/**
	* Retrieves the value of project attribute
	* @return project
	**/

	public String getProject(){
		return project;
	}

	/**
	* Sets the value of project attribute
	**/

	public void setProject(String project){
		this.project = project;
	}

	String projAndSite;
	/**
	* Retrieves the value of concatenation value of project,"//" and value of dpSiteName
	* @return projAndSite
	**/
	public String getProjAndSite() {
		return projAndSite;
	}

	/**
	* Sets the value of projAndSite attribute
	**/
	public void setProjAndSite(String projAndSite) {
		this.projAndSite = projAndSite;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.GeneralImage object's collection
	**/

	private Collection<GeneralImage> generalImageCollection;
	/**
	* Retrieves the value of generalImageCollection attribute
	* @return generalImageCollection
	**/

	public Collection<GeneralImage> getGeneralImageCollection(){
		return generalImageCollection;
	}

	/**
	* Sets the value of generalImageCollection attribute
	**/

	public void setGeneralImageCollection(Collection<GeneralImage> generalImageCollection){
		this.generalImageCollection = generalImageCollection;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.Patient object's collection
	**/

	private Collection<Patient> patientCollection;
	/**
	* Retrieves the value of patientCollection attribute
	* @return patientCollection
	**/

	public Collection<Patient> getPatientCollection(){
		return patientCollection;
	}

	/**
	* Sets the value of patientCollection attribute
	**/

	public void setPatientCollection(Collection<Patient> patientCollection){
		this.patientCollection = patientCollection;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof TrialDataProvenance)
		{
			TrialDataProvenance c =(TrialDataProvenance)obj;
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