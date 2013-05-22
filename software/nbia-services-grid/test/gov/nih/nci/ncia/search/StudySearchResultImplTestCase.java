/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class StudySearchResultImplTestCase extends TestCase {

	public void testStudySearchResultImpl() {
		NBIANode node = new NBIANode(true, "foo1", "foo2");

		SeriesSearchResult seriesSearchResultImpl0 = new SeriesSearchResult();
		SeriesSearchResult seriesSearchResultImpl1 = new SeriesSearchResult();
		SeriesSearchResult seriesSearchResultImpl2 = new SeriesSearchResult();

		SeriesSearchResult[] seriesArr = new SeriesSearchResult[3];
		seriesArr[0] = seriesSearchResultImpl0;
		seriesArr[1] = seriesSearchResultImpl1;
		seriesArr[2] = seriesSearchResultImpl2;

		StudySearchResultImpl studySearchResultImpl = new StudySearchResultImpl();
		studySearchResultImpl.setId(1);
		studySearchResultImpl.setDate(new Date(82,0,1));
		studySearchResultImpl.setDescription("d1");
		studySearchResultImpl.setOffSetDesc("o1");
		studySearchResultImpl.setStudyInstanceUid("suid1");
		studySearchResultImpl.setSeriesList(seriesArr);
		
		SeriesSearchResult notherSeries = new SeriesSearchResult();
		notherSeries.setId(4);
		
		studySearchResultImpl.setSeriesList(1, notherSeries);
		
		studySearchResultImpl.associateLocation(node);

		
		assertTrue(studySearchResultImpl.getId()==1);
		assertTrue(studySearchResultImpl.getDate().getYear()==82);
		assertTrue(studySearchResultImpl.getDescription().equals("d1"));
		assertTrue(studySearchResultImpl.getOffSetDesc().equals("o1"));
		assertTrue(studySearchResultImpl.getStudyInstanceUid().equals("suid1"));
		assertTrue(studySearchResultImpl.getSeriesList().length==3);
		assertTrue(studySearchResultImpl.getSeriesList(1).getId()==4);		
		assertTrue(studySearchResultImpl.associatedLocation().equals(node));
		
		for(SeriesSearchResult series : studySearchResultImpl.getSeriesList()) {
			assertTrue(series.associatedLocation().equals(node));
		}
	}
	
	public void testStudySearchResultImplSort() {
		StudySearchResultImpl studySearchResultImpl1 = new StudySearchResultImpl();
		studySearchResultImpl1.setDate(new Date(101,0,1));

		StudySearchResultImpl studySearchResultImpl2 = new StudySearchResultImpl();
		studySearchResultImpl2.setDate(null);
		
		StudySearchResultImpl studySearchResultImpl3 = new StudySearchResultImpl();
		studySearchResultImpl3.setDate(new Date(28,0,1));
		
		StudySearchResultImpl studySearchResultImpl4 = new StudySearchResultImpl();
		studySearchResultImpl4.setDate(new Date(1,0,1));
		
		StudySearchResultImpl studySearchResultImpl5 = new StudySearchResultImpl();
		studySearchResultImpl5.setDate(new Date(5,0,1));
		
		List<StudySearchResultImpl> list = new ArrayList<StudySearchResultImpl>();
		list.add(studySearchResultImpl1);
		list.add(studySearchResultImpl2);
		list.add(studySearchResultImpl3);
		list.add(studySearchResultImpl4);
		list.add(studySearchResultImpl5);
		
		Collections.sort(list);
	
		assertNull(list.get(0).getDate());
		assertTrue(list.get(1).getDate().getYear()==1);
		assertTrue(list.get(2).getDate().getYear()==5);
		assertTrue(list.get(3).getDate().getYear()==28);
		assertTrue(list.get(4).getDate().getYear()==101);
	}

	
	public void testStudySearchResultImplWithNoSeries() {
		NBIANode node = new NBIANode(true, "foo1", "foo2");

		StudySearchResultImpl studySearchResultImpl = new StudySearchResultImpl();
		studySearchResultImpl.associateLocation(node);
	
		assertTrue(studySearchResultImpl.associatedLocation().equals(node));	
	}	
}
