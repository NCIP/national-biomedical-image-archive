/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class StudyDetailsTestCase extends TestCase {

	public void testHappyPath() {
		
		SeriesDetails seriesDetail1 = new SeriesDetails("s1", 5);		
		SeriesDetails seriesDetail2 = new SeriesDetails("s1", 3);
		List<SeriesDetails> seriesDetails = new ArrayList<SeriesDetails>();
		seriesDetails.add(seriesDetail1);
		seriesDetails.add(seriesDetail2);
		
		StudyDetails studyDetails = new StudyDetails("studyUid", seriesDetails);
		assertTrue(studyDetails.getStudyInstanceUid().equals("studyUid"));
		assertTrue(studyDetails.getSeriesCount()==2);
		assertTrue(studyDetails.getSubmissionCount()==(5+3));
		assertTrue(studyDetails.getSeriesDetails().size()==2);
	}
	
	public void testIllegitSeries() {
		List<SeriesDetails> emptySeriesDetails = new ArrayList<SeriesDetails>();
		
		try {
			new StudyDetails("studyUid", null);
			fail();
		}
		catch(Exception ex) {
			//ok
		}
		try {
			new StudyDetails("studyUid", emptySeriesDetails);
			fail();
		}
		catch(Exception ex) {
			//ok.
		}
	}	

}
