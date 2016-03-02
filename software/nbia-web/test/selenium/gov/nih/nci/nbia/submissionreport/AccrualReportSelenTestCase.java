/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.submissionreport;

import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;


public class AccrualReportSelenTestCase extends AbstractSelenTestCaseImpl {

	public void testAccrualReportWithNoResults() throws Exception {
		login();

		navigateToSubmissionReports();

		selectSubmissionReportCollection("LIDC//LIDC");

		selectSubmissionReportDateRange("04/13/1980", "01/13/1987");

		submitAccrualReport();
        waitForEmptyAccrualResults();

		assertTrue(isThereANoResultsMessageForAccrualReport());
	}

	public void testAccrualReportValidation() throws Exception {
		login();

		navigateToSubmissionReports();

		selectSubmissionReportCollection("LIDC//LIDC");

		_testFutureDate1();
		_testFutureDate2();
		_testDateOrdering();
		_testRequireFromDate();
	}


	public void testAccrualReportWithResults() throws Exception {
		login();

		navigateToSubmissionReports();

		selectSubmissionReportCollection("LIDC//LIDC");

		selectSubmissionReportDateRange("04/13/2001", "01/13/2009");

		submitAccrualReport();
        waitForAccrualResults();


		assertTrue(getOverallCountOfImageAccrual().equals("101738"));

		assertTrue(getOverallCountOfPatientsAccrual().equals("399"));
		assertTrue(getOverallCountOfStudiesAccrual().equals("401"));
		assertTrue(getOverallCountOfSeriesAccrual().equals("401"));

		expandAccrualSubmissionReportDailyDetails(0);

		assertTrue(getNewAccrualImageCountForDay(0).equals("13577"));

		assertTrue(getNewAccrualPatientCountForDay(0).equals("76"));
		assertTrue(getNewAccrualStudyCountForDay(0).equals("77"));
		assertTrue(getNewAccrualSeriesCountForDay(0).equals("77"));
	}


	private void _testFutureDate1() throws Exception {
		selectSubmissionReportDateRange("04/13/2500", "01/13/2510");
		submitAccrualReport();
		pause(20000);

		assertTrue(getSubmissionReportErrorText().startsWith("Date Invalid:"));
	}

	private void _testFutureDate2() throws Exception {
		selectSubmissionReportDateRange("04/13/1980", "01/13/2510");
		submitAccrualReport();
		pause(20000);

		assertTrue(getSubmissionReportErrorText().startsWith("Date Invalid:"));
	}

	private void _testDateOrdering() throws Exception {
		selectSubmissionReportDateRange("04/13/1980", "01/13/1979");
		submitAccrualReport();
		pause(20000);

		assertTrue(getSubmissionReportErrorText().startsWith("Date Invalid:"));
	}

	private void _testRequireFromDate() throws Exception {
		selectSubmissionReportDateRange("", "01/13/1979");
		submitAccrualReport();
		pause(20000);

		assertTrue(getSubmissionReportErrorText().startsWith("Date Invalid:"));
	}
}
