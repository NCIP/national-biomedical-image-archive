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
package gov.nih.nci.nbia.dto;

/**
 * @author lethai
 *
 */
public class AnnotationDTO {

	public AnnotationDTO(String filePath, String fileName, String fileType, Integer fileSize){
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public Integer getFileSize() {
		return fileSize;
	}

	private String filePath;
	private String fileName;
	private String fileType;
	private Integer fileSize;
}
