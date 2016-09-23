/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.searchresult.ImageSearchResult;
import gov.nih.nci.nbia.searchresult.ImageSearchResultEx;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;
import gov.nih.nci.nbia.searchresult.StudySearchResult;

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
	public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult, String userName);


	/**
	 * For a given series id, return all the images for it.  This id is
	 * not the DICOM series instance uid, but the unique identifier at a given node (pkid).
	 */
	public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult, String userName);
	/**
	 * For a given series id, return all the images for it.  This id is
	 * not the DICOM series instance uid, but the unique identifier at a given node (pkid).
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(SeriesSearchResult seriesSearchResult, String userName);
	public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult, String userName);
	public void setThumbnailURLResolver(ThumbnailURLResolver thumbnailURLResolver);
}
