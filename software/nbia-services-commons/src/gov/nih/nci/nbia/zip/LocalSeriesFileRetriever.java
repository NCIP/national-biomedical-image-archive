/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import gov.nih.nci.nbia.dao.AnnotationDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.ImageFileDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocalSeriesFileRetriever implements SeriesFileRetriever {
	/**
	 * {@inheritDoc}
	 */
	public void cleanupResultsDirectory() {
		//we dont care about this.  pull right from the file system
	}


	/**
	 * {@inheritDoc}
	 */
	public DicomFileDTO retrieveImages(SeriesSearchResult seriesSearchResult) {
		ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
        DicomFileDTO dfd = new DicomFileDTO();

		try {
	        Map<Integer, List<ImageDTO>> dicomFilePaths = imageDAO.findKeyedImagesBySeriesPkId(Collections.singletonList(seriesSearchResult.getId()));

	        if(dicomFilePaths.size()>0) {
	            List<ImageFileDTO> imageDtoList = convert(dicomFilePaths.values().iterator().next());
	            dfd.setImageFileDTOList(imageDtoList);
	            dfd.setAnnotationDTOList(retrieveAnnotations(seriesSearchResult));
	        }
	        else {
	        	dfd.setImageFileDTOList(new ArrayList<ImageFileDTO>());
	        	dfd.setAnnotationDTOList(new ArrayList<AnnotationFileDTO>());
	        }
        	return dfd;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public List<AnnotationFileDTO> retrieveAnnotations(SeriesSearchResult seriesSearchResult) {
		if(!seriesSearchResult.isAnnotated()) {
			return new ArrayList<AnnotationFileDTO>();
		}

	    AnnotationDAO annotationDAO = (AnnotationDAO)SpringApplicationContext.getBean("annotationDAO2");

	    try {
	        Map<Integer,List<AnnotationFileDTO>> annotationFilePaths =
	        	annotationDAO.findKeyedAnnotationBySeriesPkId(Collections.singletonList(seriesSearchResult.getId()));

	        if(annotationFilePaths.size()>0) {
	            return annotationFilePaths.values().iterator().next();
	        }
	        else {
	        	return new ArrayList<AnnotationFileDTO>();
	        }
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}


	////////////////////////////////////////PRIVATE/////////////////////////////////////////

	private static List<ImageFileDTO> convert(List<ImageDTO> imageDtoList) {
    	List<ImageFileDTO> fileDtoList = new ArrayList<ImageFileDTO>();
    	for(ImageDTO imageDTO : imageDtoList) {
    		ImageFileDTO fileDto = new ImageFileDTO(imageDTO.getFileURI(),
    				                                imageDTO.getSize(),
    				                                imageDTO.getSopInstanceUid());
    		fileDtoList.add(fileDto);
    	}
    	return fileDtoList;
	}
}
