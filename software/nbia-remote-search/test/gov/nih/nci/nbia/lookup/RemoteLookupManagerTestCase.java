/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.remotesearch.RemoteNodes;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.Model;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("gov.nih.nci.nbia.remotesearch.RemoteNodes")
public class RemoteLookupManagerTestCase {


	@Test
	public void testGetUsMultiModality() throws Exception {
//		Collection<RemoteNode> fakeNodes = createFakeNodesUsMultiModality();
//
//		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
//		mockStatic(RemoteNodes.class);
//		mockStatic(NCIAConfig.class);
//
//		//set expectations for mock
//	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
//		expect(remoteNodesMock.getRemoteNodes()).
//	        andReturn(fakeNodes);
//
//
//		//replay the mock
//    	replay(NCIAConfig.class);
//    	replay(RemoteNodes.class);
//    	replay(remoteNodesMock, RemoteNodes.class);
//
//    	//verify the OUT
//    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
//    	List<String> results = remoteLookupManager.getUsMultiModality();
//    	assertEquals(results.size(), 4);
//
//    	//verify the mock
//    	verify(NCIAConfig.class);
//    	verify(RemoteNodes.class);
//    	verify(remoteNodesMock, RemoteNodes.class);
    }

	@Test
	@PrepareForTest({NCIAConfig.class, RemoteNodes.class})
	public void testGetSearchableNodes() throws Exception {
		Collection<RemoteNode> fakeNodes = createFakeNodes();

		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	Map<NBIANode, AvailableSearchTerms> results = remoteLookupManager.getSearchableNodes();
    	assertEquals(results.size(), 2);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
	}

	@Test
	public void testGetModality() throws Exception {
		Collection<RemoteNode> fakeNodes = createFakeNodesModality();

		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	List<String> results = remoteLookupManager.getModality();
    	assertEquals(results.size(), 4);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
    }

	@Test
	public void testGetAnatomicSite() throws Exception {

		Collection<RemoteNode> fakeNodes = createFakeNodesAnatomic();

		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	List<String> results = remoteLookupManager.getAnatomicSite();
    	assertEquals(results.size(), 4);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
    }

	@Test
	public void testGetSearchCollection() throws Exception {
		Collection<RemoteNode> fakeNodes = createFakeNodesCollection();

		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	List<String> results = remoteLookupManager.getSearchCollection();
    	assertEquals(results.size(), 4);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
    }

	@Test
	public void testGetDICOMKernelType() throws Exception {
		Collection<RemoteNode> fakeNodes = createFakeNodesKernels();

		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	List<String> results = remoteLookupManager.getDICOMKernelType();
    	assertEquals(results.size(), 4);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
    }

	@Test
	public void testGetManufacturerModelSoftwareItems() throws Exception {
		Collection<RemoteNode> fakeNodes = EquipmentUtilForTests.createFakeNodesEquipment();


		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

		//set expectations for mock
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);
		expect(remoteNodesMock.getRemoteNodes()).
	        andReturn(fakeNodes);


		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);
    	replay(remoteNodesMock, RemoteNodes.class);

    	//verify the OUT
    	RemoteLookupManager remoteLookupManager = new RemoteLookupManager();
    	Map<String, Map<String, Set<String>>> results = remoteLookupManager.getManufacturerModelSoftwareItems();
		//man1->m1->1,[2], 3
		//man1->m2->1,[2], 3
		//man1->m3->1,2
		//man2->m[1-3]->{1,2}
		//man3->m[1-4]->{1,2}
    	assertEquals(results.size(), 3);

    	Map<String, Set<String>> modelMap = results.get("man1");
    	assertEquals(modelMap.size(), 3);

    	assertNotNull(modelMap.get("model1"));
    	assertEquals(modelMap.get("model1").size(), 3);
    	assertNotNull(modelMap.get("model2"));
    	assertEquals(modelMap.get("model2").size(), 3);
    	assertNotNull(modelMap.get("model3"));
    	assertEquals(modelMap.get("model3").size(), 2);

    	modelMap = results.get("man2");
    	assertEquals(modelMap.size(), 3);

    	assertNotNull(modelMap.get("model1"));
    	assertEquals(modelMap.get("model1").size(), 2);
    	assertNotNull(modelMap.get("model2"));
    	assertNotNull(modelMap.get("model3"));

    	modelMap = results.get("man3");
    	assertEquals(modelMap.size(), 4);

    	assertNotNull(modelMap.get("model1"));
    	assertNotNull(modelMap.get("model2"));
    	assertNotNull(modelMap.get("model3"));
    	assertNotNull(modelMap.get("model4"));

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
    	verify(remoteNodesMock, RemoteNodes.class);
    }

	///////////////////////////////////PRIVATE/////////////////////////////////////////////

	private static Collection<RemoteNode> createFakeNodes() throws Exception {
		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0"));
		nodes.add(constructRemoteNode("http://fakeAddress1"));

		return nodes;
	}



	private static Collection<RemoteNode> createFakeNodesKernels() throws Exception {
		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		String[] kernels1 = new String[] {"k1", "k2"};
		availableSearchTerms1.setConvolutionKernels(kernels1);

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		String[] kernels2 = new String[] {"k1", "k3", "k4"};
		availableSearchTerms2.setConvolutionKernels(kernels2);

		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
	}

	private static Collection<RemoteNode> createFakeNodesCollection() throws Exception {
		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		String[] collections1 = new String[] {"c1", "c2"};
		availableSearchTerms1.setCollections(collections1);

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		String[] collections2 = new String[] {"c1", "c3", "c4"};
		availableSearchTerms2.setCollections(collections2);

		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
	}

	private static Collection<RemoteNode> createFakeNodesUsMultiModality() throws Exception {
		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		String[] modalities1 = new String[] {"m1", "m2", "m3"};
//		availableSearchTerms1.setUsMultiModalities(modalities1);

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		String[] modalities2 = new String[] {"m1", "m2", "m4"};
//		availableSearchTerms2.setUsMultiModalities(modalities2);

		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
	}

	private static Collection<RemoteNode> createFakeNodesModality() throws Exception {
		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		String[] modalities1 = new String[] {"m1", "m2", "m3"};
		availableSearchTerms1.setModalities(modalities1);

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		String[] modalities2 = new String[] {"m1", "m2", "m4"};
		availableSearchTerms2.setModalities(modalities2);

		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
	}

	private static Collection<RemoteNode> createFakeNodesAnatomic() throws Exception {
		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		String[] anatomicSites1 = new String[] {"a1", "a2", "a3"};
		availableSearchTerms1.setAnatomicSites(anatomicSites1);

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		String[] anatomicSites2 = new String[] {"a1", "a2", "a5"};
		availableSearchTerms2.setAnatomicSites(anatomicSites2);

		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
	}

//
//	String[] collections = new String[] {"c1", "c2"};
//	availableSearchTerms.setCollections(collections);
//
//	String[] kernels = new String[] {"k1", "k2", "k3", "k4"};
//	availableSearchTerms.setConvolutionKernels(kernels);
	public static RemoteNode constructRemoteNode(String address) throws Exception {

		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();

		return constructRemoteNode(address, availableSearchTerms);
	}


	public static RemoteNode constructRemoteNode(String address,
			                                     AvailableSearchTerms ast) throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);

		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI(address));


		RemoteNode remoteNode = new RemoteNode(serviceMetadata,
				                               endpointReferenceType,
				                               ast);
		return remoteNode;
	}
}
