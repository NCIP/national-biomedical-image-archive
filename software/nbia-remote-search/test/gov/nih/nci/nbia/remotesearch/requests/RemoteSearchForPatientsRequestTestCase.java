/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch.requests;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.PatientSearchResults;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SearchCriteriaDTO;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class RemoteSearchForPatientsRequestTestCase {

	@Test
	@PrepareForTest(RemoteSearchForPatientsRequest.class)
	public void testSuccesfulCall() throws Exception {
		DICOMQuery query = createQuery();
		RemoteNode remoteNode = createRemoteNode();

		//create mocks
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
            andReturn(nbiaServiceClientMock);
	    expect(nbiaServiceClientMock.searchForPatients(isA((new SearchCriteriaDTO[1]).getClass()))).
	        andReturn(createPatientResults());

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
		RemoteSearchForPatientsRequest request = new RemoteSearchForPatientsRequest(remoteNode,
				                                                                    query);
		PatientSearchResults patientSearchResults = request.call();
		assertEquals(patientSearchResults.getResults().length, 3);
		assertNull(patientSearchResults.getSearchError());
		for(PatientSearchResult result : patientSearchResults.getResults()) {
			assertEquals(result.associatedLocation(), remoteNode);
		}
    	assertEquals(patientSearchResults.getNode(), remoteNode);

    	//verify the mock
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
	}

	@Test
	@PrepareForTest(RemoteSearchForPatientsRequest.class)
	public void testFailedCall() throws Exception {
		DICOMQuery query = createQuery();
		RemoteNode remoteNode = createRemoteNode();

		//create mocks
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
            andReturn(nbiaServiceClientMock);
	    expect(nbiaServiceClientMock.searchForPatients(isA((new SearchCriteriaDTO[1]).getClass()))).
	        andThrow(new RuntimeException("something bad"));

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
		RemoteSearchForPatientsRequest request = new RemoteSearchForPatientsRequest(remoteNode,
				                                                                    query);
		PatientSearchResults patientSearchResults = request.call();
		assertNull(patientSearchResults.getResults());
		assertNotNull(patientSearchResults.getSearchError());
    	assertEquals(patientSearchResults.getNode(), remoteNode);

    	//verify the mock
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
	}

	@Test
	@PrepareForTest(RemoteSearchForPatientsRequest.class)
	public void testQueryToCriteria() throws Exception {
		DICOMQuery query = createQuery();
		RemoteNode remoteNode = createRemoteNode();

		//create mocks
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
            andReturn(nbiaServiceClientMock);
	    expect(nbiaServiceClientMock.searchForPatients(isA((new SearchCriteriaDTO[1]).getClass()))).
	        andThrow(new RuntimeException("something bad"));

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
		RemoteSearchForPatientsRequest request = new RemoteSearchForPatientsRequest(remoteNode,
				                                                                    query);
		PatientSearchResults patientSearchResults = request.call();
		assertNull(patientSearchResults.getResults());
		assertNotNull(patientSearchResults.getSearchError());
    	assertEquals(patientSearchResults.getNode(), remoteNode);

    	//verify the mock
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
	}

	///////////////////////////////PRIVATE/////////////////////////////////////////////

	private static PatientSearchResult[] createPatientResults() {
		PatientSearchResult[] patients = new PatientSearchResult[3];

		PatientSearchResultImpl p1 = new PatientSearchResultImpl();
		PatientSearchResultImpl p2 = new PatientSearchResultImpl();
		PatientSearchResultImpl p3 = new PatientSearchResultImpl();

		patients[0] = p1;
		patients[1] = p2;
		patients[2] = p3;

		return patients;
	}

	private static RemoteNode createRemoteNode() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		URI uri = new URI("http://fakeAddress");
		EndpointReferenceType endpointReferenceType = new EndpointReferenceType(uri);
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               availableSearchTerms);
		return remoteNode;
	}

	private static DICOMQuery createQuery() {
		ImageModalityCriteria imageModalityCrit = new ImageModalityCriteria();
		List<String> imageModVal = new ArrayList<String>();
		imageModVal.add("CT");
		imageModalityCrit.setImageModalityObjects(imageModVal);

		// Build Query
		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setQueryName("Query 1");
		dicomQuery.setUserID("kascice");

		dicomQuery.setCriteria(imageModalityCrit);
		return dicomQuery;
	}
}
