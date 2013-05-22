/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lethai
 *
 */
public class DownloadDataHistory implements Serializable {
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;
	private Long id;
	private Long size;
	private String loginName;
	private String seriesInstanceUid;
	private String type;
	private Date downloadTimestamp;
	public DownloadDataHistory(){		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getSeriesInstanceUid() {
		return seriesInstanceUid;
	}
	public void setSeriesInstanceUid(String seriesInstanceUid) {
		this.seriesInstanceUid = seriesInstanceUid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDownloadTimestamp() {
		return downloadTimestamp;
	}
	public void setDownloadTimestamp(Date downloadTimestamp) {
		this.downloadTimestamp = downloadTimestamp;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof DownloadDataHistory) 
		{
			DownloadDataHistory c =(DownloadDataHistory)obj; 			 
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
