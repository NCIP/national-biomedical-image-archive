/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

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
