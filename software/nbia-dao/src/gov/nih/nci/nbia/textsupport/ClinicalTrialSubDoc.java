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
	* A type of research study that tests how well new medical treatments or other interventions work in people. 	**/
public class ClinicalTrialSubDoc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* Name of the institution that is responsible for coordinating the medical imaging data for a clinical trial.	**/
	private String coordinatingCenter;
	/**
	* Retreives the value of coordinatingCenter attribute
	* @return coordinatingCenter
	**/

	public String getCoordinatingCenter(){
		return coordinatingCenter;
	}

	/**
	* Sets the value of coordinatingCenter attribue
	**/

	public void setCoordinatingCenter(String coordinatingCenter){
		this.coordinatingCenter = coordinatingCenter;
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
	* The numeric or alphanumeric identification assigned to the study by the NCI. Inter-Group protocols should use the lead Groups protocol number.	**/
	private String protocolId;
	/**
	* Retreives the value of protocolId attribute
	* @return protocolId
	**/

	public String getProtocolId(){
		return protocolId;
	}

	/**
	* Sets the value of protocolId attribue
	**/

	public void setProtocolId(String protocolId){
		this.protocolId = protocolId;
	}
	
		/**
	* Descriptive text used to represent the long title or name of a protocol.	**/
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
	* Text to capture the name of a clinical trial sponsor.	**/
	private String sponsorName;
	/**
	* Retreives the value of sponsorName attribute
	* @return sponsorName
	**/

	public String getSponsorName(){
		return sponsorName;
	}

	/**
	* Sets the value of sponsorName attribue
	**/

	public void setSponsorName(String sponsorName){
		this.sponsorName = sponsorName;
	}
	
	/**
	* An associated gov.nih.nci.ncia.domain.TrialSite object's collection 
	**/
			
	private Collection<TrialSiteDoc> siteCollection;
	/**
	* Retreives the value of siteCollection attribue
	* @return siteCollection
	**/

	public Collection<TrialSiteDoc> getSiteCollection(){
		return siteCollection;
	}

	/**
	* Sets the value of siteCollection attribue
	**/

	public void setSiteCollection(Collection<TrialSiteDoc> siteCollection){
		this.siteCollection = siteCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ClinicalTrialSubDoc) 
		{
			ClinicalTrialSubDoc c =(ClinicalTrialSubDoc)obj; 			 
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