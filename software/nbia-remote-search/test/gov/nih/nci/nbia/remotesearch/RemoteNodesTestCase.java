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
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.UsAvailableSearchTerms;
import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class RemoteNodesTestCase {

	@Test
	@PrepareForTest({RemoteNodes.class, NCIAConfig.class} )
	public void testDiscoverNodesNullReturn() throws Exception {
		String serviceAddress = "http://fakeAddress";

		//create mocks
		mockStatic(NCIAConfig.class);
		DiscoveryClient discoveryClientMock = createMock(DiscoveryClient.class);

		//set expectations for mock
	    expect(NCIAConfig.getIndexServerURL()).andReturn(serviceAddress);
		expectNew(DiscoveryClient.class, serviceAddress).
		    andReturn(discoveryClientMock);
		expect(discoveryClientMock.discoverServicesByName("NCIACoreService")).
	        andReturn(null);

		//replay the mock
    	replay(NCIAConfig.class);
    	replay(discoveryClientMock, DiscoveryClient.class);

    	//verify the OUT
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();
    	remoteNodes.discoverNodes();
    	assertEquals(remoteNodes.getRemoteNodes().size(), 0);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(discoveryClientMock, DiscoveryClient.class);
	}

	@Test
	@PrepareForTest({RemoteNodes.class, NCIAConfig.class, MetadataUtils.class} )
	public void testDiscoverNodesWithSomeMatchingEndpoints() throws Exception {
		String serviceAddress = "http://fakeAddress";

		EndpointReferenceType endpoint1 = createEndpoint(serviceAddress);
		EndpointReferenceType endpoint2 = createEndpoint(serviceAddress+"1");
		EndpointReferenceType endpoint3 = createEndpoint(serviceAddress+"2");
		EndpointReferenceType endpoint4 = new EndpointReferenceType(new Address("http://local"));

		EndpointReferenceType[] endpoints = new EndpointReferenceType[4];
		endpoints[0] = endpoint1;
		endpoints[1] = endpoint2;
		endpoints[2] = endpoint3;
		endpoints[3] = endpoint4;

		//create mocks
		mockStatic(NCIAConfig.class);
		DiscoveryClient discoveryClientMock = createMock(DiscoveryClient.class);
		NCIACoreServiceClient nbiaServiceClientMock = createMock(NCIACoreServiceClient.class);
		mockStatic(MetadataUtils.class);

		//set expectations for mock
	    expect(NCIAConfig.getIndexServerURL()).andReturn("http://index-server");
	    expect(NCIAConfig.getLocalGridURI()).andReturn("http://local").times(3);
		expectNew(DiscoveryClient.class, "http://index-server").
		    andReturn(discoveryClientMock);
		expect(discoveryClientMock.discoverServicesByName("NCIACoreService")).
	        andReturn(endpoints);

        //2 per node because of ast + uast
		expectNew(NCIACoreServiceClient.class, serviceAddress).
	        andReturn(nbiaServiceClientMock).times(2);
		expectNew(NCIACoreServiceClient.class, serviceAddress+"1").
            andReturn(nbiaServiceClientMock).times(2);

		//2 times because of 4 endpoints, 1 is 1.2, 1 is local.  that leaves 2
	    expect(nbiaServiceClientMock.getAvailableSearchTerms()).
            andReturn(new AvailableSearchTerms()).times(2);
	    expect(nbiaServiceClientMock.getUsAvailableSearchTerms()).
            andReturn(new UsAvailableSearchTerms()).times(2);

		expect(MetadataUtils.getServiceMetadata(endpoint1)).
		    andReturn(createServiceMetadataWithVersion("1.3"));
		expect(MetadataUtils.getServiceMetadata(endpoint2)).
		    andReturn(createServiceMetadataWithVersion("1.3"));
		expect(MetadataUtils.getServiceMetadata(endpoint3)).
	        andReturn(createServiceMetadataWithVersion("1.2"));
		expect(MetadataUtils.getServiceMetadata(endpoint4)).
            andReturn(createServiceMetadataWithVersion("1.3"));

		//replay the mock
    	replay(discoveryClientMock, DiscoveryClient.class);
    	replay(nbiaServiceClientMock, NCIACoreServiceClient.class);
		replay(MetadataUtils.class);
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);

    	//verify the OUT
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();
    	remoteNodes.discoverNodes();
    	assertEquals(remoteNodes.getRemoteNodes().size(), 2);

    	//verify the mock
    	verify(MetadataUtils.class);
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(discoveryClientMock, DiscoveryClient.class);
    	verify(nbiaServiceClientMock, NCIACoreServiceClient.class);
	}

	@Test
	@PrepareForTest({RemoteNodes.class, NCIAConfig.class, MetadataUtils.class} )
	public void testDiscoverNodesWithNoMatchingEndpoint() throws Exception {
		String serviceAddress = "http://fakeAddress";

		EndpointReferenceType endpoint = createEndpoint(serviceAddress);
		EndpointReferenceType[] endpoints = new EndpointReferenceType[1];
		endpoints[0] = endpoint;

		//create mocks
		mockStatic(NCIAConfig.class);
		DiscoveryClient discoveryClientMock = createMock(DiscoveryClient.class);
		mockStatic(MetadataUtils.class);

		//set expectations for mock
	    expect(NCIAConfig.getIndexServerURL()).andReturn(serviceAddress);
		expectNew(DiscoveryClient.class, serviceAddress).
		    andReturn(discoveryClientMock);
		expect(discoveryClientMock.discoverServicesByName("NCIACoreService")).
	        andReturn(endpoints);

		expect(MetadataUtils.getServiceMetadata(endpoint)).
				andReturn(createServiceMetadataWithVersion("1.2"));

		//replay the mock
		replay(MetadataUtils.class);
    	replay(NCIAConfig.class);
    	replay(discoveryClientMock, DiscoveryClient.class);

    	//verify the OUT
    	RemoteNodes remoteNodes = RemoteNodes.getInstance();
    	remoteNodes.discoverNodes();
    	assertEquals(remoteNodes.getRemoteNodes().size(), 0);

    	//verify the mock
    	verify(MetadataUtils.class);
    	verify(NCIAConfig.class);
    	verify(discoveryClientMock, DiscoveryClient.class);
	}

	//////////////////////////////////////////PRIVATE//////////////////////////////////////


	private static EndpointReferenceType createEndpoint(String address) throws Exception {
		EndpointReferenceType endpoint = new EndpointReferenceType(new Address(address));
		return endpoint;
	}

	private static ServiceMetadata createServiceMetadataWithVersion(String version) {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		Service service = new Service();
		service.setVersion(version);

		ServiceMetadataServiceDescription serviceDescription = new ServiceMetadataServiceDescription();
		serviceDescription.setService(service);

		serviceMetadata.setServiceDescription(serviceDescription);

		return serviceMetadata;
	}
}
