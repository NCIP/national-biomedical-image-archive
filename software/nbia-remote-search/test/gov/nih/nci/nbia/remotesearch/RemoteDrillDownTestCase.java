/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;
import gov.nih.nci.nbia.search.*;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;
import gov.nih.nci.ncia.search.StudySearchResultImpl;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RemoteDrillDown.class)
public class RemoteDrillDownTestCase {

	@Test
	public void testRetrieveStudyAndSeriesForPatient() throws Exception{
		PatientSearchResult patientSearchResult = createRemotePatientSearchResult();

		//create mocks
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
	        andReturn(nbiaServiceClientMock);
		expect(nbiaServiceClientMock.retrieveStudyAndSeriesForPatient(patientSearchResult)).
		    andReturn(new StudySearchResult[]{createStudySearchResult(),
		    		                          createStudySearchResult()});

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
    	RemoteDrillDown remoteDrillDown = new RemoteDrillDown();
    	StudySearchResult[] results = remoteDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
    	assertEquals(results.length, 2);
    	for(StudySearchResult result : results) {
    		RemoteNode remoteNode = (RemoteNode)result.associatedLocation();
    		assertEquals(remoteNode.getEndpointReferenceType().getAddress().toString(),
    				     serviceAddress);
    	}

    	//verify the mock
    	verify(nbiaServiceClientMock, RemoteDrillDown.class);
	}


	@Test
	public void testRetrieveImagesForSeries() throws Exception{
		SeriesSearchResult seriesSearchResult = createRemoteSeriesSearchResult();

		//create mocks
		String serviceAddress = "http://fakeAddress";
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);

		//set expectations for mock
		expectNew(NCIACoreServiceClient.class, serviceAddress).
	        andReturn(nbiaServiceClientMock);
		expect(nbiaServiceClientMock.retrieveImagesForSeries(seriesSearchResult)).
		    andReturn(new ImageSearchResult[]{createImageSearchResult(),
		    		                          createImageSearchResult()});

		//replay the mock
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);

    	//verify the OUT
    	RemoteDrillDown remoteDrillDown = new RemoteDrillDown();
    	ImageSearchResult[] results = remoteDrillDown.retrieveImagesForSeries(seriesSearchResult);
    	assertEquals(results.length, 2);
    	for(ImageSearchResult result : results) {
    		RemoteNode remoteNode = (RemoteNode)result.associatedLocation();
    		assertEquals(remoteNode.getEndpointReferenceType().getAddress().toString(),
    				     serviceAddress);
    	}

    	//verify the mock
    	verify(nbiaServiceClientMock, RemoteDrillDown.class);
	}

	/////////////////////////////////////////////PRIVATE/////////////////////////////////////

	private static SeriesSearchResult createRemoteSeriesSearchResult() throws Exception {
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(constructRemoteNode());
		return seriesSearchResult;
	}

	private static ImageSearchResult createImageSearchResult() throws Exception {
		ImageSearchResultImpl imageSearchResult = new ImageSearchResultImpl();
		return imageSearchResult;
	}

	private static StudySearchResult createStudySearchResult() throws Exception {
		StudySearchResultImpl studySearchResult = new StudySearchResultImpl();
		return studySearchResult;
	}


	private static PatientSearchResult createRemotePatientSearchResult() throws Exception {
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.associateLocation(constructRemoteNode());
		return patientSearchResult;
	}


	private static RemoteNode constructRemoteNode() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI("http://fakeAddress"));
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               availableSearchTerms);
		return remoteNode;
	}
}
