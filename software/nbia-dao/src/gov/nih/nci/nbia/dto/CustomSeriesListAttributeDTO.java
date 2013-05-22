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
 * @author lethai
 *
 */
public class CustomSeriesListAttributeDTO {
	private String seriesInstanceUid;
	private Integer id;
	private Integer parentId;
	
	public CustomSeriesListAttributeDTO(){
		
	}
	public CustomSeriesListAttributeDTO( Integer id, String seriesInstanceUid) {		
		this.id = id;
		this.seriesInstanceUid = seriesInstanceUid;
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
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getParentId() {
		return parentId;
	}
	

}
