package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class UsAvailableSearchTermsTestCase extends TestCase {

	public void testGetUsMultiModalities() {
		UsAvailableSearchTerms uast = new UsAvailableSearchTerms();
		String[] mm = new String[] {"foo1", "foo2", "foo3"};
		
		uast.setUsMultiModalities(mm);
		assertEquals(mm[0],uast.getUsMultiModalities()[0]);
		assertEquals(mm[1],uast.getUsMultiModalities()[1]);
		assertEquals(mm[2],uast.getUsMultiModalities()[2]);
		
		uast.setUsMultiModalities(1, "moo");
		assertEquals("moo",uast.getUsMultiModalities(1));

	}

}
