/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;
import junit.framework.TestCase;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;

public class RemoteNodeTestCase extends TestCase {

	public void testRemoteNodeSuccess() throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);		
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);
		
		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI("http://fakefoo"));
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata, 
				                               endpointReferenceType,
				                               availableSearchTerms);
		
		assertEquals(remoteNode.getServiceMetadata(), serviceMetadata);
		assertEquals(remoteNode.getEndpointReferenceType(), endpointReferenceType);
		assertEquals(remoteNode.getAvailableSearchTerms(), availableSearchTerms);
		assertEquals(remoteNode.getDisplayName(), "foo");
		assertFalse(remoteNode.isLocal());
		assertNotNull(remoteNode.getCreationTime());
	}
	

	public void testConstructNode() throws Exception {
		NBIANode node = RemoteNode.constructPartialRemoteNode("displayName", 
				                                              "http://foo.com");
		
		assertEquals(node.getURL(), "http://foo.com");
		assertEquals(node.getDisplayName(), "displayName");
		assertFalse(node.isLocal());		
	}	

}
