/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.savedquery;
import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;

public class SimpleSavedQuerySelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSaveAndEdit() throws Exception {
		login();

		navigateToSearchPage();

		selectModalitySearchCriteria("CT");

		selectPatientsWithAllModalities();

		selectEnhancedContrast();

		selectAnatomicalSite("BRAIN");

		selectImageSliceThickness(">=", "1 mm", "<", "4 mm");

		selectCollectionCriteria("Phantom");

		selectOnlySeriesThatHaveNoAnnotations();

		setResultsPerPage("25");

		setDateRange("08/01/2008", "08/12/2008");

		submitSearch();

		saveQuery("selenium_test_query");

		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);

		navigateToSavedQueriesPage();

		navigateToEditFirstSavedQuery();
		/////////////////////////////////////////////////////////

		verifyModality();
		verifyContrast();
		verifyAnatomicalSite();
		verifyImageSliceThickness();
		verifyCollections();
		verifyAnnotations();
		verifyDateRanges();
		verifyResultsPerPage();
	}



	private void verifyAnnotations() {
		assertTrue(!isReturnOnlySeriesWithNoAnnotationsSelected());
		assertTrue(isReturnOnlySeriesWithAnnotationsSelected());
	}

	private void verifyImageSliceThickness() {
		assertTrue(isImageSliceThicknessSelected());

		assertEquals(getImageSliceThicknessLeftValue(), "1 mm");

		assertEquals(getImageSliceThicknessRightValue(), "4 mm");

		assertEquals(getImageSliceThicknessLeftOp(), ">=");

		assertEquals(getImageSliceThicknessRightOp(), "<");
	}

	private void verifyResultsPerPage() {
		assertEquals(getSelectedNumResultsPerPage(), "25");
	}

	private void verifyCollections() {
		assertEquals(getNumCollectionsSelected(), 1);
	}

	private void verifyAnatomicalSite() {
		assertEquals(getNumAnatomicalSitesSelected(), 1);
	}

	private void verifyContrast() {
		assertTrue(!isConstrastEnhancedSelected());
		assertTrue(isContrastUnenhancedSelected());
	}

	private void verifyDateRanges() {
		assertEquals(getFromDate(), "08/01/2008");
        assertEquals(getToDate(), "08/12/2008");
	}

	private void verifyModality() {
		assertEquals(getNumModalitiesSelected(), 1);
    	assertTrue(isAllModalitySelected());
    }
}
