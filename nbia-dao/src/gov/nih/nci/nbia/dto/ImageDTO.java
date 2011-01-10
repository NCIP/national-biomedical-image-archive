package gov.nih.nci.nbia.dto;

public class ImageDTO {
	private String SOPInstanceUID;
	private String fileName;
	private Long dicomSize;
	private String project;
	private String site;
	private String ssg;
	private int frameNum;
	public ImageDTO(String SOPInstanceUID, String fileName, Long dicomSize, String project, String site, String ssg, int frameNum){
		this.SOPInstanceUID = SOPInstanceUID;
		this.fileName = fileName;
		this.dicomSize = dicomSize;
		this.project = project;
		this.site = site;
		this.ssg = ssg;
		this.frameNum = frameNum;
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
