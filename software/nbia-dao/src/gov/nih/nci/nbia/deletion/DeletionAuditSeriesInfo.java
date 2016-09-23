/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

public class DeletionAuditSeriesInfo {
	
	private String seriesInstanceUID;
	private Integer totalImageSize;
	
	public String getSeriesInstanceUID() {
		return seriesInstanceUID;
	}
	public void setSeriesInstanceUID(String seriesInstanceUID) {
		this.seriesInstanceUID = seriesInstanceUID;
	}
	public Integer getTotalImageSize() {
		return totalImageSize;
	}
	public void setTotalImageSize(Integer totalImageSize) {
		this.totalImageSize = totalImageSize;
	}
	
	

}
