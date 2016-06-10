/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.lookup.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class FileGeneratorTestCase extends TestCase {

	public void testGenerate() {
		List<BasketSeriesItemBean> seriesItems = new ArrayList<BasketSeriesItemBean>();
		BasketSeriesItemBean bsib0 = new BasketSeriesItemBean();
		bsib0.setSeriesId("series 1");
		BasketSeriesItemBean bsib1 = new BasketSeriesItemBean();
		bsib1.setSeriesId("series 2");
		BasketSeriesItemBean bsib2 = new BasketSeriesItemBean();
		bsib2.setSeriesId("series 3");
		BasketSeriesItemBean bsib3 = new BasketSeriesItemBean();
		bsib3.setSeriesId("series 4");
		BasketSeriesItemBean bsib4 = new BasketSeriesItemBean();
		bsib4.setSeriesId("series 5");
		
		seriesItems.add(bsib0);
		seriesItems.add(bsib1);
		seriesItems.add(bsib2);
		seriesItems.add(bsib3);
		seriesItems.add(bsib4);
		
		FileGenerator fg = new FileGenerator();
		String output = fg.generate(seriesItems);
		
		String[] series = output.split(",");
		for(int i=0; i< series.length; i++){
			System.out.println(" i: = " + i + " series: " + series[i]);
		}
		System.out.println("output: " + output + " length: " + series.length + " series: " + series);
		assertEquals(series.length, 6);		
	}
}