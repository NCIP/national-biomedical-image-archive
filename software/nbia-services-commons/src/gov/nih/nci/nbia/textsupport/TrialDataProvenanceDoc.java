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
	* Data (chosen by the image submitter) to describe the clinical trial and site from which the image and related data originated.	**/
public class TrialDataProvenanceDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* A numerical identifier chosen by the image submitter to identify the clinical trial site from which the image originated.	**/
	private String dpSiteId;
	/**
	* Retreives the value of dpSiteId attribute
	* @return dpSiteId
	**/

	public String getDpSiteId(){
		return dpSiteId;
	}

	/**
	* Sets the value of dpSiteId attribue
	**/

	public void setDpSiteId(String dpSiteId){
		this.dpSiteId = dpSiteId;
	}
	
		/**
	* A name chosen by the image submitter to identify the clinical trial site from which the image originated.	**/
	private String dpSiteName;
	/**
	* Retreives the value of dpSiteName attribute
	* @return dpSiteName
	**/

	public String getDpSiteName(){
		return dpSiteName;
	}

	/**
	* Sets the value of dpSiteName attribue
	**/

	public void setDpSiteName(String dpSiteName){
		this.dpSiteName = dpSiteName;
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
	* An associated gov.nih.nci.ncia.domain.GeneralImage object's collection 
	**/
			
	private Collection<GeneralImageSubDoc> generalImageCollection;
	/**
	* Retreives the value of generalImageCollection attribue
	* @return generalImageCollection
	**/

	public Collection<GeneralImageSubDoc> getGeneralImageCollection(){
		return generalImageCollection;
	}

	/**
	* Sets the value of generalImageCollection attribue
	**/

	public void setGeneralImageCollection(Collection<GeneralImageSubDoc> generalImageCollection){
		this.generalImageCollection = generalImageCollection;
	}
		

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof TrialDataProvenanceDoc) 
		{
			TrialDataProvenanceDoc c =(TrialDataProvenanceDoc)obj; 			 
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
		return "TrialDataProvenanceDoc [dpSiteId=" + dpSiteId + ", dpSiteName="
				+ dpSiteName + ", id=" + id + ", project=" + project
				+ ", generalImageCollection=" + generalImageCollection
				+ ", collectionDescription=" + collectionDescription + "]";
	}
	private String collectionDescription;
	public String getCollectionDescription() {
		return collectionDescription;
	}

	public void setCollectionDescription(String collectionDescription) {
		this.collectionDescription = collectionDescription;
	}
	
}