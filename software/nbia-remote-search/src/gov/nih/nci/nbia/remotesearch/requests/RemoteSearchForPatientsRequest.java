/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.PatientSearchResults;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;

/**
 * Encapsulates a request to search a remote node for patients.
 * It is Callable (up the hierarchy chain) so it can ultimately
 * be executed by some (asynchronous) executor.
 */
public class RemoteSearchForPatientsRequest extends RemoteRequest<PatientSearchResults> {

	/**
	 * Constructor.
	 *
	 * @param remoteNode remote node that the patient search request is bound for
	 * @param query the criteria to search for on the remote node
	 */
	public RemoteSearchForPatientsRequest(RemoteNode remoteNode,
                                          DICOMQuery query) {
		super(remoteNode);
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Invokes the searchForPatients method on the grid interface.
	 */
	public PatientSearchResults call() throws Exception {
		String serviceAddress = remoteNode.getEndpointReferenceType().getAddress().toString();

		NCIACoreServiceClient nbiaServiceClient = new NCIACoreServiceClient(serviceAddress);
		try {
			SearchCriteriaDTO[] searchCriteriaArr = DICOMQueryUtil.createSearchCriteria(query);
			PatientSearchResult[] patientResultArray = nbiaServiceClient.searchForPatients(searchCriteriaArr);
			if(patientResultArray==null) {
				patientResultArray = new PatientSearchResult[]{};
			}
			for(PatientSearchResult patientSearchResult : patientResultArray) {
				patientSearchResult.associateLocation(remoteNode);
			}

			PatientSearchResults patientSearchResults = new PatientSearchResults(remoteNode,
					                                                             patientResultArray);
			return patientSearchResults;
		}
		catch(Exception ex) {
			PatientSearchResults patientSearchResults = new PatientSearchResults(remoteNode,
                                                                                 ex);
			return patientSearchResults;
		}
	}

	///////////////////////////////////////////////PRIVATE////////////////////////////////

	private DICOMQuery query;
}
