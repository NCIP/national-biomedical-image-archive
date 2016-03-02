/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

import java.util.List;

/**
 * This object is responsible for returning pointers to DICOM image files
 * and annotation files for a given series.
 * 
 * <p>The ZipManager uses this to retrieve the actual files to be zipped up.
 * This interface is meant to mask the variances between a local file access
 * and a remote file access.
 */
public interface SeriesFileRetriever {

	/**
	 * Return pointers to DICOM image files for a given series.
	 */
	public DicomFileDTO retrieveImages(SeriesSearchResult seriesSearchResult);
	
	/**
	 * Return pointers to annotation files for a given series.
	 */	
	public List<AnnotationFileDTO> retrieveAnnotations(SeriesSearchResult seriesSearchResult);
	
	
	/**
	 * If the retriever made a copy of results.... this method will be invoked
	 * to remove the copy after the caller is done working with the results.
	 */
	public void cleanupResultsDirectory();
	
}
