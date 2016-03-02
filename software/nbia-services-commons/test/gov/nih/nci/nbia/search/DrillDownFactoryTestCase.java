/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import junit.framework.TestCase;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.searchresult.ImageSearchResult;
import gov.nih.nci.nbia.searchresult.ImageSearchResultEx;
import gov.nih.nci.nbia.searchresult.StudySearchResult;
import gov.nih.nci.nbia.searchresult.SeriesSearchResult;

public class DrillDownFactoryTestCase extends TestCase {


	public void testGetDrillDownFailure() {
		//dont set sys property "drilldown.className"

		try {
			DrillDownFactory.getDrillDown();
			fail("shouldnt get here");
		}
		catch(Exception ex) {
			//should get here
		}

	}

	public void testGetDrillDown() {
		System.setProperty("drilldown.className",
				           "gov.nih.nci.nbia.search.DrillDownFactoryTestCase$FakeDrillDown");

		DrillDown dd1 = DrillDownFactory.getDrillDown();
		DrillDown dd2 = DrillDownFactory.getDrillDown();

		assertEquals(dd1, dd2);
		assertTrue(dd1 instanceof DrillDown);
	}

	public static class FakeDrillDown implements DrillDown {
		public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult) {
			return null;
		}

		public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult) {
			return null;
		}

		public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult) {
			return null;
		}

		public ImageSearchResultEx[] retrieveImagesForSeriesEx(
				SeriesSearchResult seriesSearchResult) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
