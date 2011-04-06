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
