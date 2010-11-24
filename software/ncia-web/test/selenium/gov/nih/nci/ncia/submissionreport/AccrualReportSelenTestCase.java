package gov.nih.nci.ncia.submissionreport;

import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;


public class AccrualReportSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testAccrualReportWithNoResults() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("SUBmenu:sideMenuForm:adminToolsView:feedbackView:submissionReportMenuItem");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
                                  "30000");

		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label=LIDC//LIDC");
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/1980");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/1987");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:accrualNoResultsMsg')",
                                  "30000");

		String noResultsMsg = selenium.getText("MAINbody:accrualNoResultsMsg");
		assertTrue(noResultsMsg.startsWith("There were no submissions"));
	}

	public void testAccrualReportValidation() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("SUBmenu:sideMenuForm:adminToolsView:feedbackView:submissionReportMenuItem");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
                                  "30000");

		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label=LIDC//LIDC");

		_testFutureDate1();
		_testFutureDate2();
		_testDateOrdering();
		_testRequireFromDate();
	}


	public void testAccrualReportWithResults() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("SUBmenu:sideMenuForm:adminToolsView:feedbackView:submissionReportMenuItem");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
                                  "30000");

		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label=LIDC//LIDC");
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/2001");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/2009");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:accrualByDayForm:accrualByDayTable')",
                                  "30000");

		String overallCntTableLocator1 =
			"xpath=id('overallResultsCount')//table[1]";

		assertTrue(selenium.getTable(overallCntTableLocator1+".1.1").trim().equals("101738"));

		String overallCntTableLocator2 =
			"xpath=id('overallResultsCount')//table[2]";
		assertTrue(selenium.getTable(overallCntTableLocator2+".1.1").trim().equals("399"));
		assertTrue(selenium.getTable(overallCntTableLocator2+".2.1").trim().equals("401"));
		assertTrue(selenium.getTable(overallCntTableLocator2+".3.1").trim().equals("401"));



		selenium.click("MAINbody:accrualByDayForm:accrualByDayTable:0:accrualDayDetails");

		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:accrualByDayForm:accrualByDayTable:0:accrualDayDetailsTableContainer')",
                                  "30000");

		String firstDayTableLocator1 =
			"xpath=id('MAINbody:accrualByDayForm:accrualByDayTable:0:accrualDayDetailsTableContainer')//table[1]";
		assertTrue(selenium.getTable(firstDayTableLocator1+".1.1").trim().equals("13577"));

		String firstDayTableLocator2 =
			"xpath=id('MAINbody:accrualByDayForm:accrualByDayTable:0:accrualDayDetailsTableContainer')//table[2]";
		assertTrue(selenium.getTable(firstDayTableLocator2+".1.1").trim().equals("76"));
		assertTrue(selenium.getTable(firstDayTableLocator2+".2.1").trim().equals("77"));
		assertTrue(selenium.getTable(firstDayTableLocator2+".3.1").trim().equals("77"));

	}

	private void _testFutureDate1() throws Exception {
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/2500");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/2510");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		pause(20000);

		String errorText = selenium.getText("xpath=//table[@id='MAINbody:submissionReportCriteriaForm:submissionReportErrorField']//span");
		assertTrue(errorText.startsWith("Date Invalid:"));
	}

	private void _testFutureDate2() throws Exception {
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/1980");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/2510");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		pause(20000);

		String errorText = selenium.getText("xpath=//table[@id='MAINbody:submissionReportCriteriaForm:submissionReportErrorField']//span");
		assertTrue(errorText.startsWith("Date Invalid:"));
	}

	private void _testDateOrdering() throws Exception {
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/1980");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/1979");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		pause(20000);

		String errorText = selenium.getText("xpath=//table[@id='MAINbody:submissionReportCriteriaForm:submissionReportErrorField']//span");
		assertTrue(errorText.startsWith("Date Invalid:"));
	}

	private void _testRequireFromDate() throws Exception {
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/1979");
		selenium.click("MAINbody:submissionReportCriteriaForm:accrualReportSubmit");
		pause(20000);

		String errorText = selenium.getText("xpath=//table[@id='MAINbody:submissionReportCriteriaForm:submissionReportErrorField']//span");
		assertTrue(errorText.startsWith("Date Invalid:"));
	}
}
