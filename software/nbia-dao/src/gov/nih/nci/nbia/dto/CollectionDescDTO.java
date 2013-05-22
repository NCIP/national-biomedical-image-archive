/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.util.Date;

public class CollectionDescDTO {
	
	private String description;
	private Integer id;
	private Date collectionDescTimestamp;
	private String collectionName;
	private String userName;
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCollectionDescTimestamp() {
		return collectionDescTimestamp;
	}
	public void setCollectionDescTimestamp(Date collectionDescTimestamp) {
		this.collectionDescTimestamp = collectionDescTimestamp;
	}

}
