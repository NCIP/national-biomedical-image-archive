package gov.nih.nci.ncia.submissionreport;

import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;


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
