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
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.List;

/**
 * This implementation is smart enough to delegate the series file retrieval
 * to a local/remote retriever.
 */
public class CompositeSeriesFileRetriever implements SeriesFileRetriever {
	
	/**
	 * {@inheritDoc}
	 */	
	public List<AnnotationFileDTO> retrieveAnnotations(SeriesSearchResult seriesSearchResult){
		if(seriesSearchResult.associatedLocation().isLocal()) {
			return localSeriesFileRetriever.retrieveAnnotations(seriesSearchResult);
		}
		else {
			remoteWasRun = true;
			return remoteSeriesFileRetriever.retrieveAnnotations(seriesSearchResult);
		}	
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Only calls remote retriever to clean up and only if necessary.
	 */
	public void cleanupResultsDirectory() {
		if(remoteWasRun) {
			remoteSeriesFileRetriever.cleanupResultsDirectory();
			remoteWasRun = false;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */	
	public DicomFileDTO retrieveImages(SeriesSearchResult seriesSearchResult) {
		if(seriesSearchResult.associatedLocation().isLocal()) {
			return localSeriesFileRetriever.retrieveImages(seriesSearchResult);
		}
		else {
			remoteWasRun = true;
			return remoteSeriesFileRetriever.retrieveImages(seriesSearchResult);
		}
	}
	
	/**
	 * This method is used by remote File retrieval
	 */
	public DicomFileDTO retrieveDicomImages(SeriesSearchResult seriesSearchResult) {
		return null;
	}
	
	////////////////////////////////////////PRIVATE///////////////////////////////////////
	
	private LocalSeriesFileRetriever localSeriesFileRetriever = new LocalSeriesFileRetriever();
	
	private RemoteSeriesFileRetriever remoteSeriesFileRetriever = new RemoteSeriesFileRetriever();
	
	private boolean remoteWasRun = false;
}
