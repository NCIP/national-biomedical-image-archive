/**
 *
 */
package gov.nih.nci.ncia;

/**
 * @author lethai
 *
 */
public class AnonymousGuestSelenTestCase extends
		AbstractSelenTestCaseImpl {


		public void testPatientLevel() throws Exception {
			selenium.open("/ncia/login.jsf");
			selenium.click("link=SEARCH IMAGES");
			selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                      "30000");

			String id = selenium.getValue("xpath=id('MAINbody:searchForm:collectionsCheckboxesTable')//td[contains(text(),'RIDER Pilot')]/input/@id");
			selenium.click(id);


			selenium.select("MAINbody:searchForm:resultsPerPage", "label=25");
			selenium.click("MAINbody:searchForm:submitSearchButton");

			selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')",
                                      "30000");

			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:0:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:1:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:2:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:3:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:4:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:5:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:addPatientToBasketButton");

			//since on same page... not sure there's really a good condition
			//to wait for?
			pause(30000);

			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:6:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:7:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:8:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:9:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:10:addPatientToBasketCheckbox");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:addPatientToBasketButton");
			//since on same page... not sure there's really a good condition
			//to wait for?
			pause(30000);


			//add assert here
			//verify the fifth row contains this patient
			//assertEquals(selenium.getText("xpath=id('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')/tbody/tr[5]/td[3]/div"),
            //             "1.3.6.1.4.1.9328.50.1.0005");

			//verify saved query link is not there
			assertFalse(selenium.isElementPresent("SUBmenu:sideMenuForm:queryView:viewSavedQueriesLink"));
			//or
			assertFalse(selenium.isTextPresent("Saved queries"));

			//verify the 5th row are highlighted
			assertEquals(selenium.getAttribute("xpath=id('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')/tbody/tr[5]/td[3]/div@class"), "highlightedData");

			//error message when trying to add next 5 rows
			//assertTrue(selenium.isTextPresent("Your data basket is limited to less than 3 GB."));

		}

		public void testStudyLevel() throws Exception {
			selenium.open("/ncia/login.jsf");
			selenium.click("link=SEARCH IMAGES");
			//selenium.click("MAINbody:guestEnabled:navigationForm1:guestSearchLink");
			selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                      "30000");

			String id = selenium.getValue("xpath=id('MAINbody:searchForm:modalityCheckboxesTable')//tr[td='CT']/td//input/@id");
			selenium.click(id);

			id = selenium.getValue("xpath=id('MAINbody:searchForm:collectionsCheckboxesTable')//td[contains(text(),'RIDER Pilot')]/input/@id");
			selenium.click(id);

			selenium.select("MAINbody:searchForm:resultsPerPage", "label=25");

			selenium.click("MAINbody:searchForm:submitSearchButton");
			selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')",
                                      "30000");
			selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:7:viewPatientLink");
			selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:visualizeImages')",
                                      "30000");
			selenium.click("MAINbody:dataForm:visualizeImages");
			assertEquals("Please select a series for visualization.", selenium.getAlert());

			//selenium.click("xpath=id('MAINbody:dataForm:studyTable:0:seriesTable')/tbody/tr[0]/td[7]/div/div/input");
			//selenium.click("xpath=id('MAINbody:dataForm:studyTable:0:seriesTable')/tbody/tr[1]/td[7]/div/div/input");
			//selenium.click("xpath=id('MAINbody:dataForm:studyTable:0:seriesTable')/tbody/tr[2]/td[7]/div/div/input");
			//selenium.click("MAINbody:dataForm:studyTable:0:seriesTable:1:j_id197");
			//selenium.click("MAINbody:dataForm:studyTable:0:seriesTable:2:j_id197");


			/*assertEquals(selenium.getText("xpath=id('MAINbody:dataForm:studyTable:0:seriesTable:0:seriesUid')"),
            "1.3.6.1.4.1.9328.50.1.4435");*/

			//verify saved query link is not there
			assertFalse(selenium.isElementPresent("SUBmenu:sideMenuForm:queryView:viewSavedQueriesLink"));
			//or
			assertFalse(selenium.isTextPresent("Saved queries"));
		}
}
