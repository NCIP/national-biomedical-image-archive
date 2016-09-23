/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;


public class ImageSecurityDTO {

	public ImageSecurityDTO(String SOPInstanceUID,
			                String fileName,
			                String project,
			                String site,
			                String ssg,
			                boolean seriesVisibility,
			                int frameNum){
		this.SOPInstanceUID = SOPInstanceUID;
		this.fileName = fileName;
		this.project = project;
		this.site = site;
		this.ssg = ssg;
		this.seriesVisibility = seriesVisibility;
		this.frameNum = frameNum;
	}
	
	
	public ImageSecurityDTO(String SOPInstanceUID, String fileName,
			String project, String site, String ssg, boolean seriesVisibility,
			String frameNum) {
		this.SOPInstanceUID = SOPInstanceUID;
		this.fileName = fileName;
		this.project = project;
		this.site = site;
		this.ssg = ssg;
		this.seriesVisibility = seriesVisibility;
		if (frameNum == null) {
			this.frameNum = 0;
		}
		else {
			this.frameNum = Integer.parseInt(frameNum);
		}
	}



	public String getSOPInstanceUID() {
		return SOPInstanceUID;
	}

	public String getFileName() {
		return fileName;
	}

	public String getProject() {
		return project;
	}

	public String getSite() {
		return site;
	}

	public String getSsg() {
		return ssg;
	}

	public boolean getSeriesVisibility() {
		return seriesVisibility;
	}

	public int getFrameNum() {
			return frameNum;
	}

	private String SOPInstanceUID;
	private String fileName;
	private String project;
	private String site;
	private String ssg;
	private boolean seriesVisibility;
	private int frameNum;

}