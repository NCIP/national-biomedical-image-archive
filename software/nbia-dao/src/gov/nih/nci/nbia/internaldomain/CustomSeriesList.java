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
import java.util.HashSet;
import java.util.Set;
	/**
	* 	**/
public class CustomSeriesList  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;
	private Integer id; //hold primary key value
	private String name;
	private String comment;
	private String hyperlink;
	private Date customSeriesListTimestamp;
	private String userName;
	//private Collection<CustomSeriesListAttribute> customSeriesListAttributeCollection;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getHyperlink() {
		return hyperlink;
	}
	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}
	public Date getCustomSeriesListTimestamp() {
		return customSeriesListTimestamp;
	}
	public void setCustomSeriesListTimestamp(Date customSeriesListTimestamp) {
		this.customSeriesListTimestamp = customSeriesListTimestamp;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/*public Collection<CustomSeriesListAttribute> getCustomSeriesListAttributeCollection() {
		return customSeriesListAttributeCollection;
	}
	public void setCustomSeriesListAttributeCollection(
			Collection<CustomSeriesListAttribute> customSeriesListAttributeCollection) {
		this.customSeriesListAttributeCollection = customSeriesListAttributeCollection;
	}*/
	public void addChild(CustomSeriesListAttribute child){
		child.setParent(this);
		customSeriesListAttributes.add(child);
	}
	
	private Set<CustomSeriesListAttribute> customSeriesListAttributes = new HashSet<CustomSeriesListAttribute>();

	public Set<CustomSeriesListAttribute> getCustomSeriesListAttributes() {
		return customSeriesListAttributes;
	}
	public void setCustomSeriesListAttributes(
			Set<CustomSeriesListAttribute> customSeriesListAttributes) {
		this.customSeriesListAttributes = customSeriesListAttributes;
	}
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CustomSeriesList)
		{
			CustomSeriesList c =(CustomSeriesList)obj;
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