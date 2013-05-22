/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;
import gov.nih.nci.nbia.search.DrillDown;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.search.ThumbnailResolverFactory;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

/**
 * <p>Depending on the result object, this composite drill down will send the
 * request locally or remote.
 */
public class CompositeDrillDown implements DrillDown {
	public CompositeDrillDown() {
		localDrillDown = new LocalDrillDown();
		localDrillDown.setThumbnailURLResolver(ThumbnailResolverFactory.getThumbnailURLResolver());
	}

	/**
	 * {@inheritDoc}
	 */
	public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult) {
		if(patientSearchResult.associatedLocation().isLocal()) {
			return localDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
		}
		else {
			return remoteDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(SeriesSearchResult seriesSearchResult) {
		if(seriesSearchResult.associatedLocation().isLocal()) {
			return localDrillDown.retrieveImagesForSeriesEx(seriesSearchResult);
		}
		else {
			return remoteDrillDown.retrieveImagesForSeriesEx(seriesSearchResult);
		}
	}
	public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult) {
		if(seriesSearchResult.associatedLocation().isLocal()) {
			return localDrillDown.retrieveImagesForSeriesEx(seriesSearchResult);
		}
		else {
			return remoteDrillDown.retrieveImagesForSeriesForAllVersion(seriesSearchResult);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult) {
		if(seriesSearchResult.associatedLocation().isLocal()) {
			return localDrillDown.retrieveImagesForSeries(seriesSearchResult);
		}
		else {
			return remoteDrillDown.retrieveImagesForSeries(seriesSearchResult);
		}
	}


	private DrillDown remoteDrillDown = new RemoteDrillDown();
	private LocalDrillDown localDrillDown;
}
