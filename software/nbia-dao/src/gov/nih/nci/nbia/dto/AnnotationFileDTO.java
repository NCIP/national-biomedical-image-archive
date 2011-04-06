package gov.nih.nci.nbia.dto;

/**
 * Just information about an annotation's actual file.
 */
public class AnnotationFileDTO {

	public AnnotationFileDTO(Integer seriesPkId, String filePath, Integer fileSize){
		this.filePath = filePath;
		this.seriesPkId = seriesPkId;
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public Integer getFileSize() {
		return fileSize;
	}
	
	public Integer getSeriesPkId() {
		return seriesPkId;
	}

	private Integer seriesPkId;
	private String filePath;
	private Integer fileSize;

}
