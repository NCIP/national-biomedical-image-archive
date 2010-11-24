package gov.nih.nci.ncia.remotesearch;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.ncia.search.DrillDown;
import gov.nih.nci.ncia.search.ImageSearchResult;
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