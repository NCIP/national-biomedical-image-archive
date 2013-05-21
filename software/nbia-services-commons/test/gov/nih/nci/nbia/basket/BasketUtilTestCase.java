package gov.nih.nci.nbia.basket;

import junit.framework.TestCase;

public class BasketUtilTestCase extends TestCase {

	public void testGetSizeStringMB() {
		String size = BasketUtil.getSizeString(626743);
		assertEquals("0.627 MB", size);
	}
	
	public void testGetSizeStringGB() {
		String size = BasketUtil.getSizeString(1000626743);
		assertEquals("1.001 GB", size);
	}	
}
