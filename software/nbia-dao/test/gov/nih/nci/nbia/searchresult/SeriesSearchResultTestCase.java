/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

public class SeriesSearchResultTestCase extends TestCase {

	public void testSeriesSearchResult() {
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setId(1);
		seriesSearchResult.setAnnotated(true);
		seriesSearchResult.setAnnotationsSize(54L);
		seriesSearchResult.setDescription("d1");
		seriesSearchResult.setManufacturer("manu1");
		seriesSearchResult.setModality("CT");
		seriesSearchResult.setNumberImages(43);
		seriesSearchResult.setPatientId("p1");
		seriesSearchResult.setProject("proj1");
		seriesSearchResult.setSeriesInstanceUid("series1");
		seriesSearchResult.setSeriesNumber("3");
		seriesSearchResult.setStudyId(4);
		seriesSearchResult.setStudyInstanceUid("study1");
		seriesSearchResult.setTotalSizeForAllImagesInSeries(300L);
		
		assertTrue(seriesSearchResult.getId()==1);
		assertTrue(seriesSearchResult.isAnnotated());
		assertTrue(seriesSearchResult.getAnnotationsSize()==54L);
		assertTrue(seriesSearchResult.getDescription().equals("d1"));
		assertTrue(seriesSearchResult.getManufacturer().equals("manu1"));
		assertTrue(seriesSearchResult.getModality().equals("CT"));		
		assertTrue(seriesSearchResult.getNumberImages()==43);		
		assertTrue(seriesSearchResult.getPatientId().equals("p1"));
		assertTrue(seriesSearchResult.getProject().equals("proj1"));
		assertTrue(seriesSearchResult.getSeriesInstanceUid().equals("series1"));
		assertTrue(seriesSearchResult.getSeriesNumber().equals("3"));
		assertTrue(seriesSearchResult.getStudyId()==4);
		assertTrue(seriesSearchResult.getStudyInstanceUid().equals("study1"));
		assertTrue(seriesSearchResult.getTotalSizeForAllImagesInSeries()==300L);
		
		assertTrue(seriesSearchResult.computeExactSize()==(300+54));
	}

	public void testSeriesSearchResultNoSizeSet() {
		SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setAnnotationsSize(54L);		
		
		assertTrue(seriesSearchResult.computeExactSize()==(54));
		
		
		seriesSearchResult = new SeriesSearchResult();
		seriesSearchResult.setTotalSizeForAllImagesInSeries(300L);
		
		
		assertTrue(seriesSearchResult.computeExactSize()==(300));	
		
		seriesSearchResult = new SeriesSearchResult();		
		
		assertTrue(seriesSearchResult.computeExactSize()==(0));		
	}
	
	public void testSeriesSearchResultSort() {
		SeriesSearchResult seriesSearchResult1 = new SeriesSearchResult();
		seriesSearchResult1.setSeriesNumber("43");

		SeriesSearchResult seriesSearchResult2 = new SeriesSearchResult();
		seriesSearchResult2.setSeriesNumber(null);
		
		SeriesSearchResult seriesSearchResult3 = new SeriesSearchResult();
		seriesSearchResult3.setSeriesNumber("1");
		
		SeriesSearchResult seriesSearchResult4 = new SeriesSearchResult();
		seriesSearchResult4.setSeriesNumber(null);
		
		SeriesSearchResult seriesSearchResult5 = new SeriesSearchResult();
		seriesSearchResult5.setSeriesNumber("11");
		
		List<SeriesSearchResult> list = new ArrayList<SeriesSearchResult>();
		list.add(seriesSearchResult1);
		list.add(seriesSearchResult2);
		list.add(seriesSearchResult3);
		list.add(seriesSearchResult4);
		list.add(seriesSearchResult5);
		
		Collections.sort(list);
	
		assertTrue(list.get(0).getSeriesNumber()==(null));
		assertTrue(list.get(1).getSeriesNumber()==(null));
		assertTrue(list.get(2).getSeriesNumber().equals("1"));
		assertTrue(list.get(3).getSeriesNumber().equals("11"));
		assertTrue(list.get(4).getSeriesNumber().equals("43"));
	}
		
}
