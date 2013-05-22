/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.search.DrillDown;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.ImageSearchResultExImpl;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

/**
 * This implementation will issue grid requests to retrieve
 * the necessary drilldown results.
 */
public class RemoteDrillDown implements DrillDown {
	/**
	 * {@inheritDoc}
	 */
	public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult) {
		NBIANode nbiaNode = patientSearchResult.associatedLocation();
		assert nbiaNode.isLocal() == false;

		RemoteNode remoteNode = (RemoteNode)nbiaNode;
		String url = remoteNode.getEndpointReferenceType().getAddress().toString();
		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(url);
			StudySearchResult[] results = nbiaServiceClient.retrieveStudyAndSeriesForPatient(patientSearchResult);
			for(StudySearchResult result : results) {
				result.associateLocation(nbiaNode);
			}
			return results;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(SeriesSearchResult seriesSearchResult) {
		NBIANode nbiaNode = seriesSearchResult.associatedLocation();
		assert nbiaNode.isLocal() == false;

		RemoteNode remoteNode = (RemoteNode)nbiaNode;
		String url = remoteNode.getEndpointReferenceType().getAddress().toString();
		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(url);
			ImageSearchResultEx[] results =  nbiaServiceClient.retrieveImagesForSeriesEx(seriesSearchResult);
			for(ImageSearchResultEx result : results) {
				result.associateLocation(nbiaNode);
			}
			return results;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult) {
		NBIANode nbiaNode = seriesSearchResult.associatedLocation();
		assert nbiaNode.isLocal() == false;

		RemoteNode remoteNode = (RemoteNode)nbiaNode;

		Boolean hasEx = false;
		if (remoteNode.getUsAvailableSearchTerms() != null){
			hasEx = true;
		}

		if (hasEx){
			return retrieveImagesForSeriesEx(seriesSearchResult);
		}
		else {
			ImageSearchResult[] results =retrieveImagesForSeries(seriesSearchResult);
			int len = results.length;
			ImageSearchResultEx[] resultsEx = new ImageSearchResultEx[len];
			int i=0;
			for(ImageSearchResult result : results) {
				result.associateLocation(nbiaNode);
				ImageSearchResultEx isrei = new ImageSearchResultExImpl(result);

				resultsEx[i++] = isrei;
			}
			return resultsEx;

		}

	}
	public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult) {
		NBIANode nbiaNode = seriesSearchResult.associatedLocation();
		assert nbiaNode.isLocal() == false;

		RemoteNode remoteNode = (RemoteNode)nbiaNode;
		String url = remoteNode.getEndpointReferenceType().getAddress().toString();
		try {
			NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(url);
			ImageSearchResult[] results =  nbiaServiceClient.retrieveImagesForSeries(seriesSearchResult);
			for(ImageSearchResult result : results) {
				result.associateLocation(nbiaNode);
			}
			return results;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}