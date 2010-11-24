package gov.nih.nci.ncia.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class NBIANodeTestCase extends TestCase {

	public void testNBIANode() {
		NBIANode node = new NBIANode(true, "foo1", "foo2");
		assertTrue(node.toString().equals("foo2,foo1, local:true"));
		assertTrue(node.isLocal());
		assertTrue(node.getDisplayName().equals("foo1"));
		assertTrue(node.getURL().equals("foo2"));		
	}
	
	public void testNBIANodeSort() {
		NBIANode node1 = new NBIANode(true, "disp1", "foo2");
		NBIANode node2 = new NBIANode(true, "z", "foo2");
		NBIANode node3 = new NBIANode(true, "a", "foo2");

		List<NBIANode> list = new ArrayList<NBIANode>();
		list.add(node1);
		list.add(node2);
		list.add(node3);
		
		Collections.sort(list);

		assertTrue(list.get(0).getDisplayName().equals("a"));
		assertTrue(list.get(1).getDisplayName().equals("disp1"));
		assertTrue(list.get(2).getDisplayName().equals("z"));
	
	}	

}
