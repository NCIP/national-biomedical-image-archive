/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.lookup;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.nbia.remotesearch.RemoteNode;
import gov.nih.nci.nbia.search.*;
import gov.nih.nci.ncia.search.AvailableSearchTerms;
import gov.nih.nci.ncia.search.Manufacturer;
import gov.nih.nci.ncia.search.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.axis.message.addressing.AttributedURI;
import org.apache.axis.message.addressing.EndpointReferenceType;

public class EquipmentUtilForTests {
	public static String[] createVersions(int begin, int cnt) {
		String[] versions = new String[cnt];
		for(int i=begin;i<begin+cnt;i++) {
			versions[i-begin] = i+".0";
		}
		return versions;
	}
	
	public static Model[] createModels(int cnt) {
		return createModels(1, 1, cnt);		
	}
	
	public static Model[] createModels(int mBegin, int vBegin, int cnt) {
		Model[] models = new Model[cnt];
		for(int i=mBegin;i<mBegin+cnt;i++) {
			Model model = new Model();
			model.setName("model"+i);
			model.setVersions(createVersions(vBegin, 2));
			
			models[i-mBegin]=model;
		}
		return models;		
	}	
	
	//man1->[m1,m2]->[1.0,2.0]
	//man2->[m1,m2, m3]->[1.0,2.0]
	public static Map<String, Map<String, Set<String>>> constructEquipmentMap_manu_1_2() {
		Manufacturer manu1 = new Manufacturer();
		manu1.setName("man1");
		manu1.setModels(createModels(2));
		
		Manufacturer manu2 = new Manufacturer();
		manu2.setName("man2");
		manu2.setModels(createModels(3));
		
		Manufacturer[] manus = new Manufacturer[2];
		manus[0] = manu1;
		manus[1] = manu2;

		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		availableSearchTerms.setEquipment(manus);	
		
		return EquipmentUtil.convertEquipment(availableSearchTerms);		
	}
	
	//man2->[m2,m3,m4]->[1.0,2.0]
	//man3->[m1,m2,m3]->[1.0,2.0]	
	public static Map<String, Map<String, Set<String>>> constructEquipmentMap_manu_2_3() {
		Manufacturer manu1 = new Manufacturer();
		manu1.setName("man2");
		manu1.setModels(createModels(2,1,3));
		
		Manufacturer manu2 = new Manufacturer();
		manu2.setName("man3");
		manu2.setModels(createModels(3));
		
		Manufacturer[] manus = new Manufacturer[2];
		manus[0] = manu1;
		manus[1] = manu2;

		AvailableSearchTerms availableSearchTerms = new AvailableSearchTerms();
		availableSearchTerms.setEquipment(manus);	
		
		return EquipmentUtil.convertEquipment(availableSearchTerms);		
	}
	
	//man1->m1->1,[2], 3
	//man1->m2->1,[2], 3
	//man1->m3->1,2	
	//man2->m[1-3]->{1,2}
	//man3->m[1-4]->{1,2}
	public static Collection<RemoteNode> createFakeNodesEquipment() throws Exception {

		Manufacturer manu1 = new Manufacturer();
		manu1.setName("man1");
		manu1.setModels(createModels(2));
		
		Manufacturer manu2 = new Manufacturer();
		manu2.setName("man2");
		manu2.setModels(createModels(3));
		
		Manufacturer manu3 = new Manufacturer();
		manu3.setName("man3");
		manu3.setModels(createModels(4));	
		
		Manufacturer manu4 = new Manufacturer();
		manu4.setName("man1");
		manu4.setModels(createModels(1,2,3));		
		
		Manufacturer[] manus1 = new Manufacturer[2];
		manus1[0] = manu1;
		manus1[1] = manu2;
		
		Manufacturer[] manus2 = new Manufacturer[2];
		manus2[0] = manu3;
		manus2[1] = manu4;

		AvailableSearchTerms availableSearchTerms1 = new AvailableSearchTerms();
		availableSearchTerms1.setEquipment(manus1);			

		AvailableSearchTerms availableSearchTerms2 = new AvailableSearchTerms();
		availableSearchTerms2.setEquipment(manus2);	
		
		//null equipment too
		
		Collection<RemoteNode> nodes = new ArrayList<RemoteNode>();
		nodes.add(constructRemoteNode("http://fakeAddress0", availableSearchTerms1));
		nodes.add(constructRemoteNode("http://fakeAddress1", availableSearchTerms2));

		return nodes;
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
