/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;


/**
 * Represents customized series list for data transfer purposes
 * @author panq
 *
 */
public class QcCustomSeriesListDTO {
	private String name;
	private String userName;
	private String seriesInstanceUID;
	private String email;
	
	public QcCustomSeriesListDTO (String uName, String name, String series, String email){
		this.userName=uName;
		this.name=name;
		this.seriesInstanceUID=series;
		this.email=email;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSeriesInstanceUID() {
		return seriesInstanceUID;
	}
	public void setSeriesInstanceUIDs(String seriesInstanceUID) {
		this.seriesInstanceUID = seriesInstanceUID;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
