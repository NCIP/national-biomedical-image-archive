/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

/**
 * This object is responsible for drilling down into a patient result to return
 * details like the studies, series and images for that patient.
 *
 * <p>Used to be in something call ResultSetManager.
 */
public interface DrillDown {

	/**
	 * For a given patient, return all the studies for it.
	 */
	public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult);


	/**
	 * For a given series id, return all the images for it.  This id is
	 * not the DICOM series instance uid, but the unique identifier at a given node (pkid).
	 */
	public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult);
	/**
	 * For a given series id, return all the images for it.  This id is
	 * not the DICOM series instance uid, but the unique identifier at a given node (pkid).
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(SeriesSearchResult seriesSearchResult);
	public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult);
	public void setThumbnailURLResolver(ThumbnailURLResolver thumbnailURLResolver);
}
