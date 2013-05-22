/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author lethai
 *
 */
public class CollectionDesc  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;
	private Integer id; //hold primary key value
	private String collectionName;
	private String description;
	private Date collectionDescTimestamp;
	private String userName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCollectionDescTimestamp() {
		return collectionDescTimestamp;
	}
	public void setCollectionDescTimestamp(Date collectionDescTimestamp) {
		this.collectionDescTimestamp = collectionDescTimestamp;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CollectionDesc)
		{
			CollectionDesc c =(CollectionDesc)obj;
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