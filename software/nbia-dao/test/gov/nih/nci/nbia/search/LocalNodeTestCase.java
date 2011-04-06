package gov.nih.nci.nbia.search;

import junit.framework.TestCase;
import gov.nih.nci.ncia.search.NBIANode;

public class LocalNodeTestCase extends TestCase {

	public void testGetLocalNode() {
		System.setProperty("gov.nih.nci.ncia.grid.local.node.name", "disp1");
		System.setProperty("local.grid.uri", "url");

		NBIANode node = LocalNode.getLocalNode();
		assertTrue(node.isLocal());
		assertEquals(node.getDisplayName(),"disp1");
		assertEquals(node.getURL(), "url");
	}

}
