package gov.nih.nci.ncia.savedquery;
import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;

public class SimpleSavedQuerySelenTestCase extends AbstractSelenTestCaseImpl {

	public void testSaveAndEdit() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m16");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("MAINbody:navigationForm:searchLink");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm:modalityCheckboxesTable')",
                                  "30000");

		String id = selenium.getValue("xpath=id('MAINbody:searchForm:modalityCheckboxesTable')//tr[td='CT']/td//input/@id");
		selenium.click(id);

		selenium.click("//input[@name='MAINbody:searchForm:andSearchModalityCheck' and @value='all']");
		selenium.click("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Enhanced']");


		id = selenium.getValue("xpath=id('MAINbody:searchForm:anatomicalSitesCheckboxesTable')//tr[td='BRAIN']/td//input/@id");
		selenium.click(id);

		selenium.click("MAINbody:searchForm:imageSliceRadio");
		selenium.select("MAINbody:searchForm:cmpLeft", "label=>=");
		selenium.select("MAINbody:searchForm:cmpRight", "label=<");
		selenium.select("MAINbody:searchForm:valLeft", "label=1 mm");
		selenium.select("MAINbody:searchForm:imgThick", "label=4 mm");

		//contains finds Phantom, but = does not.  starts-with doesnt find it either...
		//not sure i understand why it wont match
		id = selenium.getValue("xpath=id('MAINbody:searchForm:collectionsCheckboxesTable')//td[contains(text(),'Phantom') and not(contains(text(),'FDA'))]/input/@id");
		selenium.click(id);

		selenium.click("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Series That Do Not Have Annotations']");
		selenium.select("MAINbody:searchForm:resultsPerPage", "label=25");
		selenium.click("MAINbody:searchForm:fromDate");
		selenium.type("MAINbody:searchForm:fromDate", "08/01/2008");
		selenium.click("MAINbody:searchForm:toDate");
		selenium.type("MAINbody:searchForm:toDate", "08/12/2008");
		selenium.click("MAINbody:searchForm:submitSearchButton");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:saveQueryView:queryName')",
                                  "30000");

		selenium.type("MAINbody:dataForm:saveQueryView:queryName", "selenium_test_query");
		selenium.click("MAINbody:dataForm:saveQueryView:saveQueryButton");
		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);
		selenium.click("link=View Saved Queries");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:queryForm:savedQueryMode:savedQueryModeDataTable')",
                                  "30000");
		//the :0: means first row in table
		//forceId from myfaces might make this more stable
		selenium.click("MAINbody:queryForm:savedQueryMode:savedQueryModeDataTable:0:editQueryButton");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm')",
                                  "30000");
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
		assertTrue(!selenium.isChecked("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Series That Do Not Have Annotations']"));
		assertTrue(selenium.isChecked("//input[@name='MAINbody:searchForm:annotationSelector' and @value='Return Only Annotated Series']"));
	}

	private void verifyImageSliceThickness() {
		assertTrue(selenium.isChecked("id=MAINbody:searchForm:imageSliceRadio"));

		String valLeft = selenium.getSelectedLabel("id=MAINbody:searchForm:valLeft");
		assertEquals(valLeft, "1 mm");

		String imgThick = selenium.getSelectedLabel("id=MAINbody:searchForm:imgThick");
		assertEquals(imgThick, "4 mm");

		String cmpLeft = selenium.getSelectedLabel("id=MAINbody:searchForm:cmpLeft");
		assertEquals(cmpLeft, ">=");

		String cmpRight = selenium.getSelectedLabel("id=MAINbody:searchForm:cmpRight");
		assertEquals(cmpRight, "<");
	}

	private void verifyResultsPerPage() {
		String resultsPerPage = selenium.getSelectedLabel("id=MAINbody:searchForm:resultsPerPage");
		assertEquals(resultsPerPage, "25");
	}

	private void verifyCollections() {
		assertEquals(selenium.getXpathCount("//table[@id='MAINbody:searchForm:selectedCollectionsTable']//tr").intValue(),
	                 1);
	}

	private void verifyAnatomicalSite() {
		assertEquals(selenium.getXpathCount("//table[@id='MAINbody:searchForm:selectedAnatomicalSitesTable']//tr").intValue(),
		             1);
	}

	private void verifyContrast() {
		assertTrue(!selenium.isChecked("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Enhanced']"));
		assertTrue(selenium.isChecked("//input[@name='MAINbody:searchForm:contrastSelector' and @value='Unenhanced']"));
	}

	private void verifyDateRanges() {
		assertEquals(selenium.getValue("//input[@name='MAINbody:searchForm:fromDate']"),
	                 "08/01/2008");
        assertEquals(selenium.getValue("//input[@name='MAINbody:searchForm:toDate']"),
                     "08/12/2008");
	}

	private void verifyModality() {
		//sanity check
		assertEquals(selenium.getXpathCount("//table[@id='MAINbody:searchForm:selectedModalitiesTable']//tr").intValue(),
			         1);

    	assertTrue(selenium.isChecked("//input[@name='MAINbody:searchForm:andSearchModalityCheck' and @value='all']"));

	}
}
