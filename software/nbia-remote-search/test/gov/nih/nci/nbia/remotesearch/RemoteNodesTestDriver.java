/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import gov.nih.nci.nbia.remotesearch.RemoteNodes;
import junit.framework.TestCase;
import java.util.*;

public class RemoteNodesTestDriver extends TestCase {

	//setting up an index server for "unit" testing..... necessary
	//to really do any reliable assertions
	public void testDiscoverNodes() throws Exception {
		RemoteNodes remoteNodes = RemoteNodes.getInstance();
		
		remoteNodes.discoverNodes();
		
		Collection<RemoteNode> nodes = remoteNodes.getRemoteNodes();
		for(RemoteNode node : nodes) {
			System.out.println(node.getServiceMetadata().getHostingResearchCenter().getResearchCenter().getDisplayName());
		}
	}

	protected void setUp() {
		System.setProperty("grid.index.url", 
				           "http://cagrid-index-stage.nci.nih.gov:8080/wsrf/services/DefaultIndexService"); 
		
	}
}
