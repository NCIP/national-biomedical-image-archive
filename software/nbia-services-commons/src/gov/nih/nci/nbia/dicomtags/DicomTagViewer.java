/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.nbia.dto.DicomTagDTO;
import gov.nih.nci.nbia.searchresult.ImageSearchResult;

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
