package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.ImageSearchResult;

import java.util.List;

/**
 * Returns all the DICOM tags for a given DICOM image file represented
 * by an image search result.  This is invoked at the "bottom" of drilldown
 * when looking at series details.
 */
public interface DicomTagViewer {

	/**
	 * Return all the DICOM tags for the specific DICOM image.
	 */
	public List<DicomTagDTO> viewDicomHeader(ImageSearchResult image);

}
