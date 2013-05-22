/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.nbia;

/**
 * @author lethai
 *
 */
public class AnonymousGuestSelenTestCase extends AbstractSelenTestCaseImpl {


	public void testPatientLevel() throws Exception {
		loginAsGuestFromTopMenu();

		selectCollectionCriteria("RIDER Pilot");
        setResultsPerPage("25");
        submitSearch();

        selectPatientsFromSearchResults(new int[]{0,1,2,3,4,5});
        final int WHICH_NODE_RESULT_NUMBER = 0;
		addPatientsToBasketFromSearchResult(WHICH_NODE_RESULT_NUMBER);

		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);

        selectPatientsFromSearchResults(new int[]{6,7,8,9,10});
		addPatientsToBasketFromSearchResult(WHICH_NODE_RESULT_NUMBER);

		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);

		//verify saved query link is not there
		assertFalse(isViewSavedQueriesLinkVisible());


		//verify the 5th row is highlighted
		assertTrue(isPatientResultHighlighted(0, 5));
	}

	public void testStudyLevel() throws Exception {
		loginAsGuestFromTopMenu();

        selectModalitySearchCriteria("CT");
		selectCollectionCriteria("RIDER Pilot");
        setResultsPerPage("25");
        submitSearch();

		final int WHICH_NODE_RESULT_NUMBER = 0;
		drillDownIntoPatientResult(WHICH_NODE_RESULT_NUMBER,7);

		selectVisualizeImages();
		assertEquals("Please select a series for visualization.", selenium.getAlert());

		//verify saved query link is not there
		assertFalse(isViewSavedQueriesLinkVisible());
	}
}
