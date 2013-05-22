/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;

public class CustomSeriesListAttribute  implements Serializable 
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;
	
    private String seriesInstanceUid;
    private Integer id;
    private Integer customSeriesListPkId;
    
    private CustomSeriesList parent;
    
	public CustomSeriesList getParent() {
		return parent;
	}
	public void setParent(CustomSeriesList parent) {
		this.parent = parent;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCustomSeriesListPkId() {
		return customSeriesListPkId;
	}
	public void setCustomSeriesListPkId(Integer customSeriesListPkId) {
		this.customSeriesListPkId = customSeriesListPkId;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CustomSeriesListAttribute)
		{
			CustomSeriesListAttribute c =(CustomSeriesListAttribute)obj;
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