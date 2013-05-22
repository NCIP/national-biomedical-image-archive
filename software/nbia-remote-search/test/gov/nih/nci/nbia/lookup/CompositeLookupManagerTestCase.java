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
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.lookup.LookupManagerImpl;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.LocalNode;
import gov.nih.nci.nbia.util.Util;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompositeLookupManager.class)
public class CompositeLookupManagerTestCase {

	@Test
	public void testGetUsMultiModalities() throws Exception {
		Util.loadSystemPropertiesFromPropertiesResource("ncia.properties");

    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getUsMultiModality()).
	        andReturn(constructStrings(3));   
	    expect(remoteLookupManagerMock.getUsMultiModality()).
            andReturn(constructStrings(12));  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	List<String> results = compositeLookupManager.getUsMultiModality();
    	assertEquals(results.size(), 12); //3 subset of 12 for str1-str12
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
	}
	
	@Test
	public void testGetModalities() throws Exception {
		Util.loadSystemPropertiesFromPropertiesResource("ncia.properties");

    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getModality()).
	        andReturn(constructStrings(2));   
	    expect(remoteLookupManagerMock.getModality()).
            andReturn(constructStrings(7));  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	List<String> results = compositeLookupManager.getModality();
    	assertEquals(results.size(), 7);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
	}
	
	
	@Test
	public void testGetSearchableNodes() throws Exception {
    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getSearchableNodes()).
	        andReturn(constructLocalLookupMgrNodeMap());   
	    expect(remoteLookupManagerMock.getSearchableNodes()).
            andReturn(constructRemoteLookupMgrNodeMap());  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	Map<NBIANode, AvailableSearchTerms> results = compositeLookupManager.getSearchableNodes();
    	assertEquals(results.size(), 3);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
	}
	

	@Test
	public void testGetAnatomicSite() throws Exception {
    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getAnatomicSite()).
	        andReturn(constructStrings(2));   
	    expect(remoteLookupManagerMock.getAnatomicSite()).
            andReturn(constructStrings(7));  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	List<String> results = compositeLookupManager.getAnatomicSite();
    	assertEquals(results.size(), 7);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
	}

	@Test
	public void testGetSearchCollection() throws Exception {
    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getSearchCollection()).
	        andReturn(constructStrings(2));   
	    expect(remoteLookupManagerMock.getSearchCollection()).
            andReturn(constructStrings(7));  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	List<String> results = compositeLookupManager.getSearchCollection();
    	assertEquals(results.size(), 7);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
    }

	@Test
	public void testGetDICOMKernelType() throws Exception {
    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getDICOMKernelType()).
	        andReturn(constructStrings(2));   
	    expect(remoteLookupManagerMock.getDICOMKernelType()).
            andReturn(constructStrings(7));  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	List<String> results = compositeLookupManager.getDICOMKernelType();
    	assertEquals(results.size(), 7);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
	}

	@Test
	public void testGetManufacturerModelSoftwareItems() throws Exception {
    	Collection<String> authorizedCollections = new ArrayList<String>();
    	authorizedCollections.add("foo1");
    	authorizedCollections.add("foo2");
    	
		LookupManagerImpl localLookupManagerMock = createMock(LookupManagerImpl.class);
		RemoteLookupManager remoteLookupManagerMock = createMock(RemoteLookupManager.class);

		//set expectations for mock
		expectNew(RemoteLookupManager.class).
	        andReturn(remoteLookupManagerMock);		
		expectNew(LookupManagerImpl.class, authorizedCollections).
            andReturn(localLookupManagerMock);
		
	    expect(localLookupManagerMock.getManufacturerModelSoftwareItems()).
	        andReturn(EquipmentUtilForTests.constructEquipmentMap_manu_1_2());   
	    expect(remoteLookupManagerMock.getManufacturerModelSoftwareItems()).
            andReturn(EquipmentUtilForTests.constructEquipmentMap_manu_2_3());  	    
		
		//replay the mock
    	replay(localLookupManagerMock, LookupManagerImpl.class); 
    	replay(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
    	//verify the OUT
    	CompositeLookupManager compositeLookupManager = new CompositeLookupManager(authorizedCollections);
    	Map<String, Map<String, Set<String>>> results = compositeLookupManager.getManufacturerModelSoftwareItems();
    	assertEquals(results.size(), 3);
    	
    	assertEquals(results.get("man1").size(),2);
    	assertEquals(results.get("man1").get("model1").size(),2);
    	assertEquals(results.get("man1").get("model1").size(),2);    	
    	assertEquals(results.get("man2").size(),4);
    	assertEquals(results.get("man2").get("model2").size(),2);
    	assertEquals(results.get("man2").get("model2").size(),2);    	
    	assertEquals(results.get("man3").size(),3);
    	
    	//verify the mock
    	verify(localLookupManagerMock, LookupManagerImpl.class); 
    	verify(remoteLookupManagerMock, RemoteLookupManager.class); 
    	
	}

	////////////////////////////////////////PRIVATE/////////////////////////////////////////
	
	private static List<String> constructStrings(int cnt) {
		List<String> strings = new ArrayList<String>();
		for(int i=0;i<cnt;i++) {
			strings.add("str"+i);
		}
		return strings;
	}
	private static Map<NBIANode, AvailableSearchTerms> constructRemoteLookupMgrNodeMap() throws Exception {
    	Map<NBIANode, AvailableSearchTerms> map =  new LinkedHashMap<NBIANode, AvailableSearchTerms>();
    	map.put(constructRemoteNode("http://fakeAddress"), new AvailableSearchTerms());
    	map.put(constructRemoteNode("http://fakeAddress1"), new AvailableSearchTerms());

    	return map;		
	}
	
	private static Map<NBIANode, AvailableSearchTerms> constructLocalLookupMgrNodeMap() {
    	Map<NBIANode, AvailableSearchTerms> map =  new LinkedHashMap<NBIANode, AvailableSearchTerms>();
    	map.put(LocalNode.getLocalNode(), new AvailableSearchTerms());
    	return map;		
	}

	
	private static RemoteNode constructRemoteNode(String addr) throws Exception {
		ServiceMetadata serviceMetadata = new ServiceMetadata();
		ServiceMetadataHostingResearchCenter serviceMetadataHostingResearchCenter = new ServiceMetadataHostingResearchCenter();
		ResearchCenter researchCenter = new ResearchCenter();
		researchCenter.setDisplayName("foo");
		serviceMetadataHostingResearchCenter.setResearchCenter(researchCenter);		
		serviceMetadata.setHostingResearchCenter(serviceMetadataHostingResearchCenter);
		
		EndpointReferenceType endpointReferenceType = new EndpointReferenceType();
		endpointReferenceType.setAddress(new AttributedURI(addr));
		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		RemoteNode remoteNode = new RemoteNode(serviceMetadata, 
				                               endpointReferenceType,
				                               availableSearchTerms);	
		return remoteNode;
	}		
}
