package gov.nih.nci.nbia.basket;

import junit.framework.TestCase;

public class BasketSeriesItemBeanTestCase extends TestCase {

	
	public void testGetAnnotationSize3Digits() {
		BasketSeriesItemBean item = new BasketSeriesItemBean();
		item.setAnnotationsSize(626743L);
		String size = item.getAnnotationSize3Digits();
		assertEquals(size, "0.627");
	}
}
