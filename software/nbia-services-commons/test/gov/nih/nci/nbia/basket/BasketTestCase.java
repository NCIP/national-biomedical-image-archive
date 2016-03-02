/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class BasketTestCase extends TestCase {

	public void testCalculateAnnotationSizeInBytes() throws Exception {
		Basket basket = constructBasket();
		double annoSize = basket.calculateAnnotationSizeInBytes();
		assertEquals(annoSize, 100.0);
	}
	
	////////////////////////////////////////PRIVATE////////////////////////////////////////
	
	private static Basket constructBasket() throws Exception {
		Basket basket = new Basket();					
		basket.addSeries(constructSeriesResults());
		return basket;
	}
	
	
	private static List<SeriesSearchResult> constructSeriesResults() {
		List<SeriesSearchResult> seriesItems = new ArrayList<SeriesSearchResult>();
		
		SeriesSearchResult result = new SeriesSearchResult();
		result.setAnnotationsSize(100L);
		seriesItems.add(result);
		
		return seriesItems;
	}
}
