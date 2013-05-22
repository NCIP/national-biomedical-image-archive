/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class DailyReportWrapperTestCase extends TestCase {

	public void testEmpty() {
		DailyReportWrapper dailyReportWrapper = new DailyReportWrapper(null, 3);
		assertTrue(dailyReportWrapper.isReportEmpty());
		assertEquals(dailyReportWrapper.getSubmissionCount(), 3);
		assertFalse(dailyReportWrapper.isExpanded());
	}


	public void testNotEmpty() {
		PatientDetailGroupWrapper pdgw1 = createPatientDetailGroupWrapper();
		PatientDetailGroupWrapper pdgw2 = createPatientDetailGroupWrapper();

		List<PatientDetailGroupWrapper> list = new ArrayList<PatientDetailGroupWrapper>();
		list.add(pdgw1);
		list.add(pdgw2);

		DailyReportWrapper dailyReportWrapper = new DailyReportWrapper(list, 23);
		dailyReportWrapper.setExpanded(true);
		assertFalse(dailyReportWrapper.isReportEmpty());
		assertEquals(dailyReportWrapper.getSubmissionCount(), 23);
		assertTrue(dailyReportWrapper.isExpanded());
		assertEquals(dailyReportWrapper.getPatientDetails().size(), 2);

	}

	private static PatientDetailGroupWrapper createPatientDetailGroupWrapper() {
		PatientDetailGroupWrapper patientDetailWrapper0  =
			new PatientDetailGroupWrapper("patient0",
					                      "study0",
					                      "series0",
					                      "img0",
					                      new ArrayList<PatientDetailGroupWrapper>());


		PatientDetailGroupWrapper patientDetailWrapper1  =
			new PatientDetailGroupWrapper("patient1",
					                      "study1",
					                      "series1",
					                      "img1",
					                      new ArrayList<PatientDetailGroupWrapper>());
		List<PatientDetailGroupWrapper> children = new ArrayList<PatientDetailGroupWrapper>();
		children.add(patientDetailWrapper0);
		children.add(patientDetailWrapper1);

		PatientDetailGroupWrapper patientDetailWrapper_mid  =
			new PatientDetailGroupWrapper("patientmid",
					                      "studymid",
					                      "seriesmid",
					                      "imgmid",
					                      children);

		children = new ArrayList<PatientDetailGroupWrapper>();
		children.add(patientDetailWrapper_mid);

		PatientDetailGroupWrapper patientDetailGroupWrapper
			= new PatientDetailGroupWrapper("patienttop",
                                            "studytop",
                                            "seriestop",
                                            "imgtop",
                                            children);
		return patientDetailGroupWrapper;
	}

}
