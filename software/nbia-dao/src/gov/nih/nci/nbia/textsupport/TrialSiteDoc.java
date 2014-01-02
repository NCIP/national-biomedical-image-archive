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
	* The site responsible for submitting clinical trial data. 	**/
public class TrialSiteDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
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
	* One or more characters used as the identifier of the site responsible for submitting clinical trial data.	**/
	private String trialSiteId;
	/**
	* Retreives the value of trialSiteId attribute
	* @return trialSiteId
	**/

	public String getTrialSiteId(){
		return trialSiteId;
	}

	/**
	* Sets the value of trialSiteId attribue
	**/

	public void setTrialSiteId(String trialSiteId){
		this.trialSiteId = trialSiteId;
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
	* An associated gov.nih.nci.ncia.domain.ClinicalTrial object
	**/
			
	private ClinicalTrialSubDoc trial;
	/**
	* Retreives the value of trial attribue
	* @return trial
	**/
	
	public ClinicalTrialSubDoc getTrial(){
		return trial;
	}
	/**
	* Sets the value of trial attribue
	**/

	public void setTrial(ClinicalTrialSubDoc trial){
		this.trial = trial;
	}
			

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof TrialSiteDoc) 
		{
			TrialSiteDoc c =(TrialSiteDoc)obj; 			 
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