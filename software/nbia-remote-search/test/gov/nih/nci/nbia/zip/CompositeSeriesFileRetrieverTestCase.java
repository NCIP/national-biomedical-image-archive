/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.zip;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.expectLastCall;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.dto.AnnotationFileDTO;
import gov.nih.nci.nbia.dto.DicomFileDTO;
import gov.nih.nci.nbia.dto.ImageFileDTO;
import gov.nih.nci.nbia.remotesearch.RemoteDrillDown;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.LocalDrillDown;
import gov.nih.nci.nbia.zip.LocalSeriesFileRetriever;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompositeSeriesFileRetriever.class)
public class CompositeSeriesFileRetrieverTestCase {

	@Test
	public void testRetrieveAnnotationsRemote() throws Exception{
		NBIANode localNode = constructRemoteNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		List<AnnotationFileDTO> fakeResults = new ArrayList<AnnotationFileDTO>();
		fakeResults.add(new AnnotationFileDTO(0,"", 0));
		fakeResults.add(new AnnotationFileDTO(0,"", 0));

		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);	
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);
		expect(remoteSeriesFileRetrieverMock.retrieveAnnotations(seriesSearchResult)).
            andReturn(fakeResults);
		
		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	List<AnnotationFileDTO> results = compositeSeriesFileRetriever.retrieveAnnotations(seriesSearchResult);
    	assertEquals(results.size(), 2);
    	
    	//verify the mock
    	verify(localSeriesFileRetrieverMock, LocalDrillDown.class);                
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
	}
	
	@Test
	public void testRetrieveAnnotationsLocal() throws Exception{
		NBIANode localNode = constructLocalNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		List<AnnotationFileDTO> fakeResults = new ArrayList<AnnotationFileDTO>();
		fakeResults.add(new AnnotationFileDTO(0,"", 0));
		fakeResults.add(new AnnotationFileDTO(0,"", 0));

		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);
		expect(localSeriesFileRetrieverMock.retrieveAnnotations(seriesSearchResult)).
            andReturn(fakeResults);	 		
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);			
		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
   	
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	List<AnnotationFileDTO> results = compositeSeriesFileRetriever.retrieveAnnotations(seriesSearchResult);
    	assertEquals(results.size(), 2);
    	
    	//verify the mock
    	verify(localSeriesFileRetrieverMock, LocalDrillDown.class);                
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
	}	

	@Test
	public void testRetrieveImagesLocal() throws Exception{
		NBIANode localNode = constructLocalNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		DicomFileDTO fakeResult = new DicomFileDTO();
		List<ImageFileDTO> fakeResults = new ArrayList<ImageFileDTO>();
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResult.setImageFileDTOList(fakeResults);
		
		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);
		expect(localSeriesFileRetrieverMock.retrieveImages(seriesSearchResult)).
            andReturn(fakeResult);	 		
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);			
		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
   	
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	gov.nih.nci.nbia.dto.DicomFileDTO result = compositeSeriesFileRetriever.retrieveImages(seriesSearchResult);
    	List<ImageFileDTO> results = result.getImageFileDTOList(); 
    	assertEquals(results.size(), 2);
    	
    	//verify the mock
    	verify(localSeriesFileRetrieverMock, LocalDrillDown.class);                
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
	}	
	
	@Test
	public void testRetrieveImagesRemote() throws Exception{
		NBIANode localNode = constructRemoteNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		DicomFileDTO fakeResult = new DicomFileDTO();
		List<ImageFileDTO> fakeResults = new ArrayList<ImageFileDTO>();
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResult.setImageFileDTOList(fakeResults);
		
		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);	
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);
		expect(remoteSeriesFileRetrieverMock.retrieveImages(seriesSearchResult)).
            andReturn(fakeResult);	 	
		
		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
   	
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	gov.nih.nci.nbia.dto.DicomFileDTO result = compositeSeriesFileRetriever.retrieveImages(seriesSearchResult);
    	List<ImageFileDTO> results = result.getImageFileDTOList();
    	assertEquals(results.size(), 2);
    	
    	//verify the mock
    	verify(localSeriesFileRetrieverMock, LocalDrillDown.class);                
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
	}		
	
	
	@Test
	public void testCleanupRemote() throws Exception{
		NBIANode localNode = constructRemoteNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		DicomFileDTO fakeResult = new DicomFileDTO();
		List<ImageFileDTO> fakeResults = new ArrayList<ImageFileDTO>();
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResult.setImageFileDTOList(fakeResults);
		
		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);	
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);
		expect(remoteSeriesFileRetrieverMock.retrieveImages(seriesSearchResult)).
            andReturn(fakeResult);	 		
		remoteSeriesFileRetrieverMock.cleanupResultsDirectory();
		expectLastCall();	 	

		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
   	
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	gov.nih.nci.nbia.dto.DicomFileDTO result = compositeSeriesFileRetriever.retrieveImages(seriesSearchResult);
    	List<ImageFileDTO> results = result.getImageFileDTOList();
    	compositeSeriesFileRetriever.cleanupResultsDirectory();
    	assertEquals(results.size(), 2);
    	
    	//verify the mock
    	verify(localSeriesFileRetrieverMock, LocalDrillDown.class);                
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
	}	
	
	@Test
	public void testCleanupLocal() throws Exception{
		NBIANode localNode = constructLocalNode();
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.associateLocation(localNode);
		
		DicomFileDTO fakeResult = new DicomFileDTO();
		List<ImageFileDTO> fakeResults = new ArrayList<ImageFileDTO>();
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResults.add(new ImageFileDTO("",new Long(0),""));
		fakeResult.setImageFileDTOList(fakeResults);
		
		//create mocks
		LocalSeriesFileRetriever localSeriesFileRetrieverMock = createMock(LocalSeriesFileRetriever.class);
		RemoteSeriesFileRetriever remoteSeriesFileRetrieverMock = createMock(RemoteSeriesFileRetriever.class);
		
		//set expectations for mock
		expectNew(LocalSeriesFileRetriever.class).
		    andReturn(localSeriesFileRetrieverMock);
		expect(localSeriesFileRetrieverMock.retrieveImages(seriesSearchResult)).
            andReturn(fakeResult);			
		expectNew(RemoteSeriesFileRetriever.class).
	        andReturn(remoteSeriesFileRetrieverMock);
		localSeriesFileRetrieverMock.cleanupResultsDirectory();
		expectLastCall().
		    andThrow(new RuntimeException("shouldnt call"));	 	
		
		//replay the mock
    	replay(localSeriesFileRetrieverMock, LocalSeriesFileRetriever.class);
    	replay(remoteSeriesFileRetrieverMock, RemoteSeriesFileRetriever.class);                

    	//verify the OUT
    	CompositeSeriesFileRetriever compositeSeriesFileRetriever = new CompositeSeriesFileRetriever();
    	DicomFileDTO result = compositeSeriesFileRetriever.retrieveImages(seriesSearchResult);
    	List<ImageFileDTO> results = result.getImageFileDTOList();
    	compositeSeriesFileRetriever.cleanupResultsDirectory();
    	assertEquals(results.size(), 2);
    	
    	//verify the mock              
    	verify(remoteSeriesFileRetrieverMock, RemoteDrillDown.class); 
    	//dont verify LocalSeriesFileRetriever - it will think getInstance should have been invoked
    	//and it shouldnt have been if code is working correctly - we are really
    	//doing a negative asserrtion throw the "andThrow" expectation.    	
	}	
	
	/////////////////////////////////////////PRIVATE//////////////////////////////////////
	
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
}
