/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.databasket;
import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;

public class AddPatientToDataBasketSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testAddPatient() throws Exception {
		login();

		navigateToSearchPage();

		selectModalitySearchCriteria("CT");

		selectCollectionCriteria("RIDER Pilot");

		submitSearch();

		addFirstPatientOfFirstNodeResultsToBasket();

		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);

		navigateToDataBasketPage();

		///////////////////////////////////////////////////////////////////////

		verifyPatientIds();
		verifyStudyIds();
		verifySeriesIds();
	}

	//////////////////////////////////PRIVATE//////////////////////////////////////////////////////

	private void verifyPatientIds() {
        assertEquals(getNumOfRowsInDataBasket(),
       	             12);
        assertEquals(getPatientIdFromDataBasket(3),
                     "1.3.6.1.4.1.9328.50.1.0001");
	}


	private void verifyStudyIds() {

        assertEquals(getStudyIdFromDataBasket(1),
                     "1.3.6.1.4.1.9328.50.1.139");
        assertEquals(getStudyIdFromDataBasket(2),
                     "1.3.6.1.4.1.9328.50.1.139");
        assertEquals(getStudyIdFromDataBasket(3),
                     "1.3.6.1.4.1.9328.50.1.139");


        assertEquals(getStudyIdFromDataBasket(4),
                     "1.3.6.1.4.1.9328.50.1.2");
        assertEquals(getStudyIdFromDataBasket(5),
                     "1.3.6.1.4.1.9328.50.1.2");
        assertEquals(getStudyIdFromDataBasket(6),
                     "1.3.6.1.4.1.9328.50.1.2");


        assertEquals(getStudyIdFromDataBasket(7),
                     "1.3.6.1.4.1.9328.50.1.275");
        assertEquals(getStudyIdFromDataBasket(8),
                     "1.3.6.1.4.1.9328.50.1.275");
        assertEquals(getStudyIdFromDataBasket(9),
                     "1.3.6.1.4.1.9328.50.1.275");

        assertEquals(getStudyIdFromDataBasket(10),
                     "1.3.6.1.4.1.9328.50.1.401");
        assertEquals(getStudyIdFromDataBasket(11),
                     "1.3.6.1.4.1.9328.50.1.401");
        assertEquals(getStudyIdFromDataBasket(12),
                     "1.3.6.1.4.1.9328.50.1.401");
	}

	private void verifySeriesIds() {

        assertEquals(getSeriesIdFromDataBasket(1),
                     "1.3.6.1.4.1.9328.50.1.140");
        assertEquals(getSeriesIdFromDataBasket(2),
                     "1.3.6.1.4.1.9328.50.1.144");
        assertEquals(getSeriesIdFromDataBasket(3),
                     "1.3.6.1.4.1.9328.50.1.217");


        assertEquals(getSeriesIdFromDataBasket(4),
                     "1.3.6.1.4.1.9328.50.1.3");
        assertEquals(getSeriesIdFromDataBasket(5),
                     "1.3.6.1.4.1.9328.50.1.7");
        assertEquals(getSeriesIdFromDataBasket(6),
                     "1.3.6.1.4.1.9328.50.1.81");


        assertEquals(getSeriesIdFromDataBasket(7),
                     "1.3.6.1.4.1.9328.50.1.276");
        assertEquals(getSeriesIdFromDataBasket(8),
                     "1.3.6.1.4.1.9328.50.1.280");
        assertEquals(getSeriesIdFromDataBasket(9),
                     "1.3.6.1.4.1.9328.50.1.345");

        assertEquals(getSeriesIdFromDataBasket(10),
                     "1.3.6.1.4.1.9328.50.1.402");
        assertEquals(getSeriesIdFromDataBasket(11),
                     "1.3.6.1.4.1.9328.50.1.406");
        assertEquals(getSeriesIdFromDataBasket(12),
                     "1.3.6.1.4.1.9328.50.1.522");
	}

}
