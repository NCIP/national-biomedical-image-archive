/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.nbia.querystorage.PersistentQuery;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;
import gov.nih.nci.nbia.security.NCIAUser;

import java.io.Serializable;
	/**
	* 	**/
public class SavedQuery  implements Serializable , PersistentQuery 
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;
	
    private SavedQueryLastExec lastExecuteDate;

	
		/**
	* 	**/
	private Boolean active;
	/**
	* Retreives the value of active attribute
	* @return active
	**/

	public Boolean getActive(){
		return active;
	}

	/**
	* Sets the value of active attribue
	**/

	public void setActive(Boolean active){
		this.active = active;
	}
	
		/**
	* 	**/
	private Long id;
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
	* 	**/
	private Boolean newResults;
	/**
	* Retreives the value of newResults attribute
	* @return newResults
	**/

	public Boolean getNewResults(){
		return newResults;
	}

	/**
	* Sets the value of newResults attribue
	**/

	public void setNewResults(Boolean newResults){
		this.newResults = newResults;
	}
	
		/**
	* 	**/
	private String queryName;
	/**
	* Retreives the value of queryName attribute
	* @return queryName
	**/

	public String getQueryName(){
		return queryName;
	}

	/**
	* Sets the value of queryName attribue
	**/

	public void setQueryName(String queryName){
		this.queryName = queryName;
	}
	
		/**
	* 	**/
	private Long userId;
	/**
	* Retreives the value of userId attribute
	* @return userId
	**/

	public Long getUserId(){
		return userId;
	}

	/**
	* Sets the value of userId attribue
	**/

	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	/**
	* An associated gov.nih.nci.ncia.security.NCIAUser object
	**/
			
	private NCIAUser user;
	/**
	* Retreives the value of user attribue
	* @return user
	**/
	
	public NCIAUser getUser(){
		return user;
	}
	/**
	* Sets the value of user attribue
	**/

	public void setUser(NCIAUser user){
		this.user = user;
	}
	
	public SavedQueryLastExec getLastExecuteDate() {
        return lastExecuteDate;
    }

    public void setLastExecuteDate(SavedQueryLastExec lastExecuteDate) {
        this.lastExecuteDate = lastExecuteDate;
    }
	/**
	* An associated gov.nih.nci.ncia.domain.SavedQueryAttribute object's collection 
	**/
			
	private Collection<SavedQueryAttribute> savedQueryAttributeCollection;
	/**
	* Retreives the value of savedQueryAttributeCollection attribue
	* @return savedQueryAttributeCollection
	**/

	public Collection<SavedQueryAttribute> getSavedQueryAttributeCollection(){
		return savedQueryAttributeCollection;
	}

	/**
	* Sets the value of savedQueryAttributeCollection attribue
	**/

	public void setSavedQueryAttributeCollection(Collection<SavedQueryAttribute> savedQueryAttributeCollection){
		this.savedQueryAttributeCollection = savedQueryAttributeCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof SavedQuery) 
		{
			SavedQuery c =(SavedQuery)obj; 			 
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


	
	private Set<SavedQueryAttribute> savedQueryAttributes;

    public SavedQuery() {
        savedQueryAttributes = new HashSet<SavedQueryAttribute>();
    }

    public void addQueryAttribute(QueryAttributeWrapper attr, int instanceNumber) {
        SavedQueryAttribute sqa = new SavedQueryAttribute(attr, this, instanceNumber);
        savedQueryAttributes.add(sqa);
    }

    public void removeSavedQueryAttribute(SavedQueryAttribute attr) {
        savedQueryAttributes.remove(attr);
    }
    
    public Set<SavedQueryAttribute> getSavedQueryAttributes() {
        return savedQueryAttributes;
    }

    public void setSavedQueryAttributes(
        Set<SavedQueryAttribute> savedQueryAttributes) {
        this.savedQueryAttributes = savedQueryAttributes;
    }



}