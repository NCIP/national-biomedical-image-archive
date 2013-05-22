/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.submissionreport;

import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;


public class AnnotationSubmissionReportSelenTestCase extends AbstractSelenTestCaseImpl {
	public void testAnnotationSubmissionReportWithNoResults() throws Exception {
		login();

        navigateToSubmissionReports();

		selectSubmissionReportCollection("LIDC//LIDC");

		selectSubmissionReportDateRange("04/13/1980", "01/13/1987");

		submitAnnotationReport();
        waitForEmptyAnnotationResults();

		String noResultsMsg = selenium.getText("MAINbody:annotationNoResultsMsg");
		assertTrue(noResultsMsg.startsWith("There were no submissions"));
	}

	public void testAnnotationSubmissionReportWithResults() throws Exception {
		login();

        navigateToSubmissionReports();

		selectSubmissionReportCollection("LIDC//LIDC");

		selectSubmissionReportDateRange("04/13/2001", "06/01/2009");

		submitAnnotationReport();

		waitForNonEmptyAnnotationResults();

		//////////////////////////////////////////////////////////////

		assertOverallCountsTable();

		//assertDetailsTable();
	}

	private void assertOverallCountsTable() {

		assertTrue(getOverallCountOfPatientsAffected().equals("387"));
		assertTrue(getOverallCountOfStudiesAffected().equals("389"));
		assertTrue(getOverallCountOfSeriesAffected().equals("389"));
	}

	/*private void assertDetailsTable() throws Exception {

		expandAnnotationSubmissionReportDailyDetails(0);

        final int FIRST_DAY = 0;
        final int FIRST_PATIENT = 1;
		assertTrue(getNthPatientIdForAnnotationSubmissionReportDay(FIRST_DAY, FIRST_PATIENT).equals("1.3.6.1.4.1.9328.50.3.0043"));
		assertTrue(getNthPatientNumStudiesForAnnotationSubmissionReportDay(FIRST_DAY, FIRST_PATIENT).equals("1"));
		assertTrue(getNthPatientNumSeriesForAnnotationSubmissionReportDay(FIRST_DAY, FIRST_PATIENT).equals("1"));
		assertTrue(getNthPatientNumAnnotationsForAnnotationSubmissionReportDay(FIRST_DAY, FIRST_PATIENT).equals("1"));

		//expand patient
		expandOrCollapseInAnnotationSubmissionReportDailyDetails(FIRST_DAY,0);

		assertTrue(getNthStudyIdForPatientAnnotationSubmissionReportDay(0,1,1).equals("1.3.6.1.4.1.9328.50.3.6324"));
		assertTrue(getNthNumSeriesForPatientAnnotationSubmissionReportDay(0,1,1).equals("1"));
		assertTrue(getNthNumAnnotationsForPatientAnnotationSubmissionReportDay(0,1,1).equals("1"));

		//expand study?
		expandOrCollapseInAnnotationSubmissionReportDailyDetails(FIRST_DAY,1);
		pause(30000);  //alternative is to wait for count of table rows to increase. this is lazy but easy

		assertTrue(getSeriesId(FIRST_DAY, 1, 1, 1).equals("1.3.6.1.4.1.9328.50.3.6325"));
		assertTrue(getNumAnnotationsInSeries(FIRST_DAY, 1, 1, 1).equals("1"));


		expandOrCollapseInAnnotationSubmissionReportDailyDetails(FIRST_DAY,0);

		assertTrue(getNumOfPatientsForAnnotationSubmissionReportDay(FIRST_DAY)==65);
	}*/
}
