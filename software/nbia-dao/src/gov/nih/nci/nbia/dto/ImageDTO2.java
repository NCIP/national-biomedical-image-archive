/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

public class ImageDTO2 {
	private String SOPInstanceUID;
	private String fileName;
	private Long dicomSize;
	private String project;
	private String site;
	private String ssg;
	private int frameNum;
	public ImageDTO2(String SOPInstanceUID, String fileName, Long dicomSize, String project, String site, String ssg, int frameNum){
		this.SOPInstanceUID = SOPInstanceUID;
		this.fileName = fileName;
		this.dicomSize = dicomSize;
		this.project = project;
		this.site = site;
		this.ssg = ssg;
		this.frameNum = frameNum;
	}
	
	public ImageDTO2(String SOPInstanceUID, String fileName, Long dicomSize, String project, String site, String ssg, String frameNum){
		this.SOPInstanceUID = SOPInstanceUID;
		this.fileName = fileName;
		this.dicomSize = dicomSize;
		this.project = project;
		this.site = site;
		this.ssg = ssg;
		if (frameNum == null){
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

	public Long getDicomSize() {
		return dicomSize;
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

	public int getFrameNum() {
		return frameNum;
	}
}
