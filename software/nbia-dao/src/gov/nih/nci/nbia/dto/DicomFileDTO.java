package gov.nih.nci.nbia.dto;

import java.util.List;

public class DicomFileDTO {
	
	private List<AnnotationFileDTO> annotationDTOList;
	private List<ImageFileDTO> imageFileDTOList;
	
	public List<AnnotationFileDTO> getAnnotationDTOList() {
		return annotationDTOList;
	}
	public void setAnnotationDTOList(List<AnnotationFileDTO> annotationDTOList) {
		this.annotationDTOList = annotationDTOList;
	}
	public List<ImageFileDTO> getImageFileDTOList() {
		return imageFileDTOList;
	}
	public void setImageFileDTOList(List<ImageFileDTO> imageFileDTOList) {
		this.imageFileDTOList = imageFileDTOList;
	}

}
