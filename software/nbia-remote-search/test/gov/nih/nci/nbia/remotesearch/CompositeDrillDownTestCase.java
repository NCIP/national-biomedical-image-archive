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
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.search.ThumbnailResolverFactory;
import gov.nih.nci.nbia.search.ThumbnailURLResolver;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.PatientSearchResultImpl;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompositeDrillDown.class)
public class CompositeDrillDownTestCase {

	@Test
	public void testRetrieveStudyAndSeriesForPatientRemote() throws Exception{
		PatientSearchResult patientSearchResult = createRemotePatientSearchResult();

		System.setProperty("thumbnailResolver.className",
				           "gov.nih.nci.nbia.remotesearch.CompositeDrillDownTestCase$DummyThumbnailURLResolver");
		ThumbnailURLResolver resolver = ThumbnailResolverFactory.getThumbnailURLResolver();

		//create mocks
		LocalDrillDown localDrillDownMock = createMock(LocalDrillDown.class);
		RemoteDrillDown remoteDrillDownMock = createMock(RemoteDrillDown.class);

		//set expectations for mock
		expectNew(LocalDrillDown.class).
		    andReturn(localDrillDownMock);
		localDrillDownMock.setThumbnailURLResolver(resolver);
		expectLastCall();
		expectNew(RemoteDrillDown.class).
	        andReturn(remoteDrillDownMock);
		expect(remoteDrillDownMock.retrieveStudyAndSeriesForPatient(patientSearchResult)).
		    andReturn(new StudySearchResult[]{null, null});

		//replay the mock
    	replay(localDrillDownMock, LocalDrillDown.class);
    	replay(remoteDrillDownMock, RemoteDrillDown.class);

    	//verify the OUT
    	CompositeDrillDown compositeDrillDown = new CompositeDrillDown();
    	StudySearchResult[] results = compositeDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
    	assertEquals(results.length, 2);

    	//verify the mock
    	verify(localDrillDownMock, LocalDrillDown.class);
    	verify(remoteDrillDownMock, RemoteDrillDown.class);
	}

	@Test
	public void testRetrieveStudyAndSeriesForPatientLocal() throws Exception{
		PatientSearchResult patientSearchResult = createLocalPatientSearchResult();

		System.setProperty("thumbnailResolver.className",
				           "gov.nih.nci.nbia.remotesearch.CompositeDrillDownTestCase$DummyThumbnailURLResolver");
		ThumbnailURLResolver resolver = ThumbnailResolverFactory.getThumbnailURLResolver();

		//create mocks
		LocalDrillDown localDrillDownMock = createMock(LocalDrillDown.class);
		RemoteDrillDown remoteDrillDownMock = createMock(RemoteDrillDown.class);

		//set expectations for mock
		expectNew(LocalDrillDown.class).
		    andReturn(localDrillDownMock);
		localDrillDownMock.setThumbnailURLResolver(resolver);
		expectLastCall();
		expect(localDrillDownMock.retrieveStudyAndSeriesForPatient(patientSearchResult)).
	    andReturn(new StudySearchResult[]{null, null});
		expectNew(RemoteDrillDown.class).
	        andReturn(remoteDrillDownMock);

		//replay the mock
    	replay(localDrillDownMock, LocalDrillDown.class);
    	replay(remoteDrillDownMock, RemoteDrillDown.class);

    	//verify the OUT
    	CompositeDrillDown compositeDrillDown = new CompositeDrillDown();
    	StudySearchResult[] results = compositeDrillDown.retrieveStudyAndSeriesForPatient(patientSearchResult);
    	assertEquals(results.length, 2);

    	//verify the mock
    	verify(localDrillDownMock, LocalDrillDown.class);
    	verify(remoteDrillDownMock, RemoteDrillDown.class);
	}

	@Test
	public void testRetrieveImagesForSeriesRemote() throws Exception {
		SeriesSearchResult seriesSearchResult = createRemoteSeriesSearchResult();

		System.setProperty("thumbnailResolver.className",
				           "gov.nih.nci.nbia.remotesearch.CompositeDrillDownTestCase$DummyThumbnailURLResolver");
		ThumbnailURLResolver resolver = ThumbnailResolverFactory.getThumbnailURLResolver();

		//create mocks
		LocalDrillDown localDrillDownMock = createMock(LocalDrillDown.class);
		RemoteDrillDown remoteDrillDownMock = createMock(RemoteDrillDown.class);

		//set expectations for mock
		expectNew(LocalDrillDown.class).
		    andReturn(localDrillDownMock);
		localDrillDownMock.setThumbnailURLResolver(resolver);
		expectLastCall();
		expectNew(RemoteDrillDown.class).
	        andReturn(remoteDrillDownMock);
		expect(remoteDrillDownMock.retrieveImagesForSeries(seriesSearchResult)).
		    andReturn(new ImageSearchResult[]{null, null});

		//replay the mock
    	replay(localDrillDownMock, LocalDrillDown.class);
    	replay(remoteDrillDownMock, RemoteDrillDown.class);

    	//verify the OUT
    	CompositeDrillDown compositeDrillDown = new CompositeDrillDown();
    	ImageSearchResult[] results = compositeDrillDown.retrieveImagesForSeries(seriesSearchResult);
    	assertEquals(results.length, 2);

    	//verify the mock
    	verify(localDrillDownMock, LocalDrillDown.class);
    	verify(remoteDrillDownMock, RemoteDrillDown.class);
    }

	@Test
	public void testRetrieveImagesForSeriesLocal() throws Exception {
		SeriesSearchResult seriesSearchResult = createLocalSeriesSearchResult();

		System.setProperty("thumbnailResolver.className",
				           "gov.nih.nci.nbia.remotesearch.CompositeDrillDownTestCase$DummyThumbnailURLResolver");
		ThumbnailURLResolver resolver = ThumbnailResolverFactory.getThumbnailURLResolver();

		//create mocks
		LocalDrillDown localDrillDownMock = createMock(LocalDrillDown.class);
		RemoteDrillDown remoteDrillDownMock = createMock(RemoteDrillDown.class);

		//set expectations for mock
		expectNew(LocalDrillDown.class).
		    andReturn(localDrillDownMock);
		localDrillDownMock.setThumbnailURLResolver(resolver);
		expectLastCall();
		expect(localDrillDownMock.retrieveImagesForSeries(seriesSearchResult)).
	    andReturn(new ImageSearchResult[]{null, null});
		expectNew(RemoteDrillDown.class).
	        andReturn(remoteDrillDownMock);

		//replay the mock
    	replay(localDrillDownMock, LocalDrillDown.class);
    	replay(remoteDrillDownMock, RemoteDrillDown.class);

    	//verify the OUT
    	CompositeDrillDown compositeDrillDown = new CompositeDrillDown();
    	ImageSearchResult[] results = compositeDrillDown.retrieveImagesForSeries(seriesSearchResult);
    	assertEquals(results.length, 2);

    	//verify the mock
    	verify(localDrillDownMock, LocalDrillDown.class);
    	verify(remoteDrillDownMock, RemoteDrillDown.class);
    }

	///////////////////////////////////////////PRIVATE///////////////////////////////////

	private static SeriesSearchResult createRemoteSeriesSearchResult() throws Exception {
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(constructRemoteNode());
		return seriesSearchResult;
	}

	private static SeriesSearchResult createLocalSeriesSearchResult() throws Exception {
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(constructLocalNode());
		return seriesSearchResult;
	}

	private static PatientSearchResult createRemotePatientSearchResult() throws Exception {
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.associateLocation(constructRemoteNode());
		return patientSearchResult;
	}

	private static PatientSearchResult createLocalPatientSearchResult() throws Exception {
		PatientSearchResultImpl patientSearchResult = new PatientSearchResultImpl();
		patientSearchResult.associateLocation(constructLocalNode());
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

	private static NBIANode constructLocalNode() throws Exception {
		NBIANode localNode = new NBIANode(true,
				                          "displayName",
				                          "http://fakeAddress");
		return localNode;
	}

	public static class DummyThumbnailURLResolver implements ThumbnailURLResolver{

		public String resolveThumbnailUrl(ImageDTO imageDto) {
			return "foo";
		}
	}
}
