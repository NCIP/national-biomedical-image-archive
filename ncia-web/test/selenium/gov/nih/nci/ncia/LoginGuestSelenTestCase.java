package gov.nih.nci.ncia;

public class LoginGuestSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		loginAsGuest();

		selectModalitySearchCriteria("CT");

		submitSearch();
		
		//simple incidental test. make sure first RIDER patient shows up
        //assertEquals(selenium.getText("xpath=id('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')/tbody/tr[1]/td[3]/div"),
        //             "1.3.6.1.4.1.9328.50.1.0001");
	}
}
