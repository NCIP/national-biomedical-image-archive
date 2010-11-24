package gov.nih.nci.ncia.submissionreport;

import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;


public class AnnotationSubmissionReportSelenTestCase extends AbstractSelenTestCaseImpl {
	public void testAnnotationSubmissionReportWithNoResults() throws Exception {
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
		selenium.click("MAINbody:submissionReportCriteriaForm:annotationReportSubmit");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:annotationNoResultsMsg')",
                                  "30000");

		String noResultsMsg = selenium.getText("MAINbody:annotationNoResultsMsg");
		assertTrue(noResultsMsg.startsWith("There were no submissions"));
	}

	public void testAnnotationSubmissionReportWithResults() throws Exception {
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
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "06/01/2009");
		selenium.click("MAINbody:submissionReportCriteriaForm:annotationReportSubmit");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('overallResultsCount')",
                                  "30000");

		//////////////////////////////////////////////////////////////

		assertOverallCountsTable();

		//underlying data has changed
		//assertDetailsTable();
	}

	private void assertOverallCountsTable() {

		String overallCntTableLocator =
			"xpath=id('overallResultsCount')//table[1]";
		assertTrue(selenium.getTable(overallCntTableLocator+".1.1").trim().equals("387"));
		assertTrue(selenium.getTable(overallCntTableLocator+".2.1").trim().equals("389"));
		assertTrue(selenium.getTable(overallCntTableLocator+".3.1").trim().equals("389"));
	}

	private void assertDetailsTable() throws Exception {

		String patientDetailsTable =
			"MAINbody:annotationByDayForm:annotationByDayTable:0:patientDetailsTable";

		selenium.click("MAINbody:annotationByDayForm:annotationByDayTable:0:expand");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('"+patientDetailsTable+"')",
                                  "30000");

		assertTrue(selenium.getTable(patientDetailsTable+".1.0").trim().equals("1.3.6.1.4.1.9328.50.3.0043"));
		assertTrue(selenium.getTable(patientDetailsTable+".1.1").trim().equals("1"));
		assertTrue(selenium.getTable(patientDetailsTable+".1.2").trim().equals("1"));
		assertTrue(selenium.getTable(patientDetailsTable+".1.3").trim().equals("1"));

		//expand patient

		selenium.click(patientDetailsTable+":0:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy

		assertTrue(selenium.getTable(patientDetailsTable+".2.1").trim().equals("1.3.6.1.4.1.9328.50.3.6324"));
		assertTrue(selenium.getTable(patientDetailsTable+".2.2").trim().equals("1"));
		assertTrue(selenium.getTable(patientDetailsTable+".2.3").trim().equals("1"));

		//expand study?
		selenium.click(patientDetailsTable+":1:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy

		assertTrue(selenium.getTable(patientDetailsTable+".3.2").trim().equals("1.3.6.1.4.1.9328.50.3.6325"));
		assertTrue(selenium.getTable(patientDetailsTable+".3.3").trim().equals("1"));


		//collapse and count table
		selenium.click(patientDetailsTable+":0:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy
		assertTrue(selenium.getXpathCount("id('"+patientDetailsTable+"')//tr").intValue()==65);
	}
}
