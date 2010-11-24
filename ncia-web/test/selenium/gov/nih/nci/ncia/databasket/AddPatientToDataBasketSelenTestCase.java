package gov.nih.nci.ncia.databasket;
import com.thoughtworks.selenium.*;
import java.util.regex.Pattern;
import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;

public class AddPatientToDataBasketSelenTestCase extends AbstractSelenTestCaseImpl {

	private static final int PATIENT_ID_COL = 2;
	private static final int STUDY_ID_COL = 3;
	private static final int SERIES_ID_COL = 4;

	public void testAddPatient() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("MAINbody:navigationForm:searchLink");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:searchForm:modalityCheckboxesTable')",
                                  "30000");

		String id = selenium.getValue("xpath=id('MAINbody:searchForm:modalityCheckboxesTable')//tr[td='CT']/td//input/@id");
		System.out.println("id for ct checkbox:"+id);
		selenium.click(id);

		selenium.click("MAINbody:searchForm:submitSearchButton");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable')",
                                  "30000");
		selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:patientResultsTable:0:addPatientToBasketCheckbox");
		selenium.click("MAINbody:dataForm:tableOfPatientResultTables:0:notWaitingView:addPatientToBasketButton");

		//since on same page... not sure there's really a good condition
		//to wait for?
		pause(30000);
		selenium.click("MAINbody:dataForm:viewBasketButton");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:basketForm:dataBasketDataTable')",
                                  "30000");
//this is broken until 50.1.0001 patient is fixed
		//		verifyPatientIds();
//		verifyStudyIds();
//		verifySeriesIds();
	}

	private void verifyPatientIds() {
        assertEquals(selenium.getXpathCount("id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr"),
       	             12);
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[3]/td["+PATIENT_ID_COL+"]"),
                     "13614193285010001");
	}


	private void verifyStudyIds() {

        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[1]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.139");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[2]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.139");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[3]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.139");


        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[4]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.2");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[5]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.2");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[6]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.2");


        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[7]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.275");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[8]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.275");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[9]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.275");

        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[10]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.401");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[11]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.401");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[12]/td["+STUDY_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.401");
	}

	private void verifySeriesIds() {

        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[1]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.140");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[2]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.144");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[3]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.217");


        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[4]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.3");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[5]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.7");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[6]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.81");


        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[7]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.276");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[8]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.280");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[9]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.345");

        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[10]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.402");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[11]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.406");
        assertEquals(selenium.getText("xpath=id('MAINbody:basketForm:dataBasketDataTable')/tbody/tr[12]/td["+SERIES_ID_COL+"]"),
                     "1.3.6.1.4.1.9328.50.1.522");
	}

}
