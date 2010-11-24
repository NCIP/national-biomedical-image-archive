package gov.nih.nci.ncia;

public class LoginGuestSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSanity() throws Exception {
		selenium.open("/ncia/login.jsf");
		selenium.click("MAINbody:guestEnabled:navigationForm1:guestSearchLink");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                  "30000");

		String id = selenium.getValue("xpath=id('MAINbody:searchForm:modalityCheckboxesTable')//tr[td='CT']/td//input/@id");
		selenium.click(id);

		selenium.click("MAINbody:searchForm:submitSearchButton");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')",
                                  "30000");
		//simple incidental test. make sure first RIDER patient shows up
        //assertEquals(selenium.getText("xpath=id('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')/tbody/tr[1]/td[3]/div"),
        //             "1.3.6.1.4.1.9328.50.1.0001");
	}
}
