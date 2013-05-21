package gov.nih.nci.nbia.submissionreport;

import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;


public class ImageSubmissionReportSelenTestCase extends AbstractSelenTestCaseImpl {
	public void testImageSubmissionReportWithNoResults() throws Exception {
		login();

        navigateToSubmissionReports();

        selectSubmissionReportCollection("TCGA//Henry Ford");
        selectSubmissionReportDateRange("04/13/1980", "01/13/1987");
        submitImageSubmissionReport();

        waitForImageSubmissionReportResults();

  		assertTrue(isThereANoResultsMessageForImageSubmissionReport());
	}
}
