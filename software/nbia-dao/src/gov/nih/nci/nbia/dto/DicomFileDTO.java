/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

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
