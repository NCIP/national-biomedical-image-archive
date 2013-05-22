/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dicomtags;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.ncia.dto.DicomTagDTO;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;
import gov.nih.nci.ncia.search.NBIANode;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;

public class DicomTagUtil {
	public static NBIANode constructLocalNode() {
		NBIANode localNode = new NBIANode(true, "foo", "http://fakeAddress"); 
		return localNode;
	}
	
	public static RemoteNode constructRemoteNode() throws Exception {
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
	
	
	public static ImageSearchResult constructImageSearchResultRemote() throws Exception {
		ImageSearchResultImpl imageSearchResult = new ImageSearchResultImpl();
		imageSearchResult.setId(666);
		imageSearchResult.associateLocation(constructRemoteNode());
		return imageSearchResult;
	}
	
	public static ImageSearchResult constructImageSearchResultLocal() throws Exception {
		ImageSearchResultImpl imageSearchResult = new ImageSearchResultImpl();
		imageSearchResult.setId(6667);
		imageSearchResult.associateLocation(constructLocalNode());
		return imageSearchResult;
	}	
	
	
	public static DicomTagDTO[] constructMockReturnValue() {
		DicomTagDTO[] mockDicomReturn = new DicomTagDTO[4];
		
		DicomTagDTO dto0 = new DicomTagDTO("el0", "name0", "data0");
		DicomTagDTO dto1 = new DicomTagDTO("el1", "name1", "data1");
		DicomTagDTO dto2 = new DicomTagDTO("el2", "name2", "data2");
		DicomTagDTO dto3 = new DicomTagDTO("el3", "name3", "data3");
		
		mockDicomReturn[0] = dto0;
		mockDicomReturn[1] = dto1;
		mockDicomReturn[2] = dto2;
		mockDicomReturn[3] = dto3;
		return mockDicomReturn;		
	}
}
