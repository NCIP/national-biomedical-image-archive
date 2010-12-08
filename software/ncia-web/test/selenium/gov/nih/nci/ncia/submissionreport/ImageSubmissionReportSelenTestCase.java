package gov.nih.nci.ncia.submissionreport;

import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;


public class ImageSubmissionReportSelenTestCase extends AbstractSelenTestCaseImpl {
	public void testImageSubmissionReportWithNoResults() throws Exception {
		selenium.open("/ncia/");
		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m16");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
                                  "30000");

		selenium.click("SUBmenu:sideMenuForm:adminToolsView:feedbackView:submissionReportMenuItem");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
                                  "30000");

		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label=TCGA//Henry Ford");
		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/1980");
		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "01/13/1987");
		selenium.click("MAINbody:submissionReportCriteriaForm:imageReportSubmit");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:imageSubmissionsByDayForm:imageSubmissionPanelTabSet')",
                                  "30000");

		String noResultsMsg = selenium.getText("MAINbody:imageSubmissionsByDayForm:imageSubmissionPanelTabSet:0:noNewImagesPanel");
		assertTrue(noResultsMsg.startsWith("There were no submissions"));
	}

	//comment this out till we get henry ford back
//	public void testImageSubmissionReportWithResults() throws Exception {
//		selenium.open("/ncia/");
//		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
//		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m16");
//		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
//		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:navigationForm:searchLink')",
//                                  "30000");
//
//		selenium.click("SUBmenu:sideMenuForm:toolsView:feedbackView:submissionReportMenuItem");
//		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:submissionReportCriteriaForm:collectionSiteMenu')",
//                                  "30000");
//
//		selenium.select("MAINbody:submissionReportCriteriaForm:collectionSiteMenu", "label=TCGA//Henry Ford");
//		selenium.type("MAINbody:submissionReportCriteriaForm:fromDate", "04/13/2007");
//		selenium.type("MAINbody:submissionReportCriteriaForm:toDate", "06/01/2009");
//		selenium.click("MAINbody:submissionReportCriteriaForm:imageReportSubmit");
//		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:imageSubmissionsByDayForm:imageSubmissionPanelTabSet')",
//                                  "30000");
//
//		//////////////////////////////////////////////////////////////
//
//		assertOverallCountsTable();
//
//		assertDetailsTable();
//	}

	private void assertOverallCountsTable() {
		String overallCntTableLocator1 =
			"xpath=id('overallResultsCount')//table[1]";

		System.out.println("1.1:"+selenium.getTable(overallCntTableLocator1+".1.1").trim());
		assertTrue(selenium.getTable(overallCntTableLocator1+".1.1").trim().equals("43176"));

		String overallCntTableLocator2 =
			"xpath=id('overallResultsCount')//table[2]";
		assertTrue(selenium.getTable(overallCntTableLocator2+".1.1").trim().equals("27"));
		assertTrue(selenium.getTable(overallCntTableLocator2+".2.1").trim().equals("29"));
		assertTrue(selenium.getTable(overallCntTableLocator2+".3.1").trim().equals("317"));
	}

	private void assertDetailsTable() throws Exception {
		String patientDetailsTable = "MAINbody:imageSubmissionsByDayForm:imageSubmissionPanelTabSet:0:imageSubmissionsByDayTable:4:patientDetailsTable";

		selenium.click("MAINbody:imageSubmissionsByDayForm:imageSubmissionPanelTabSet:0:imageSubmissionsByDayTable:4:expand");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('"+patientDetailsTable+"')",
                                  "30000");

		assertTrue(selenium.getTable(patientDetailsTable+".3.1").trim().equals("1"));
		System.out.println("2.2:"+selenium.getTable(patientDetailsTable+".3.2"));
		assertTrue(selenium.getTable(patientDetailsTable+".3.2").trim().equals("4"));
		assertTrue(selenium.getTable(patientDetailsTable+".3.3").trim().equals("120"));

		//expand patient
		selenium.click(patientDetailsTable+":2:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy

		assertTrue(selenium.getTable(patientDetailsTable+".4.1").trim().equals("1.3.6.1.4.1.9328.50.45.320561828852391248976513340399338344562"));
		assertTrue(selenium.getTable(patientDetailsTable+".4.2").trim().equals("4"));
		assertTrue(selenium.getTable(patientDetailsTable+".4.3").trim().equals("120"));

		//expand study?
		selenium.click(patientDetailsTable+":3:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy

		System.out.println("5.3:"+selenium.getTable(patientDetailsTable+".5.2"));
		assertTrue(selenium.getTable(patientDetailsTable+".5.2").trim().equals("1.3.6.1.4.1.9328.50.45.307913219979179773844175465549050514698"));
		assertTrue(selenium.getTable(patientDetailsTable+".5.3").trim().equals("30"));
		assertTrue(selenium.getTable(patientDetailsTable+".6.2").trim().equals("1.3.6.1.4.1.9328.50.45.298333720306108827748007107675419845370"));
		assertTrue(selenium.getTable(patientDetailsTable+".6.3").trim().equals("18"));
		assertTrue(selenium.getTable(patientDetailsTable+".7.2").trim().equals("1.3.6.1.4.1.9328.50.45.174867326345850615411832058212611283564"));
		assertTrue(selenium.getTable(patientDetailsTable+".7.3").trim().equals("42"));
		assertTrue(selenium.getTable(patientDetailsTable+".8.2").trim().equals("1.3.6.1.4.1.9328.50.45.45297429354433369251870152979516237770"));
		assertTrue(selenium.getTable(patientDetailsTable+".8.3").trim().equals("30"));

		//collapse and count table
		selenium.click(patientDetailsTable+":2:expandContractImage");
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy
		assertTrue(selenium.getXpathCount("id('"+patientDetailsTable+"')//tr").intValue()==4);
	}
}
