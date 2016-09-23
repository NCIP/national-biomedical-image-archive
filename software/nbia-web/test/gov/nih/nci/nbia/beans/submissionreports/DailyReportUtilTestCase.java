/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import gov.nih.nci.nbia.verifysubmission.PatientDetails;
import gov.nih.nci.nbia.verifysubmission.SeriesDetails;
import gov.nih.nci.nbia.verifysubmission.StudyDetails;
import gov.nih.nci.nbia.beans.submissionreports.DailyReportUtil.DailyReportWrapperFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

public class DailyReportUtilTestCase extends TestCase {

	public void testConvert() {
		SeriesDetails seriesDetail1 = new SeriesDetails("s1", 5);
		SeriesDetails seriesDetail2 = new SeriesDetails("s2", 3);
		SeriesDetails seriesDetail3 = new SeriesDetails("s2", 2);
		List<SeriesDetails> seriesDetails = new ArrayList<SeriesDetails>();
		seriesDetails.add(seriesDetail1);
		seriesDetails.add(seriesDetail2);
		seriesDetails.add(seriesDetail3);
		StudyDetails studyDetails1 = new StudyDetails("studyUid1", seriesDetails);
		StudyDetails studyDetails2 = new StudyDetails("studyUid2", seriesDetails);
		List<StudyDetails> studyDetails = new ArrayList<StudyDetails>();
		studyDetails.add(studyDetails1);
		studyDetails.add(studyDetails2);
		PatientDetails patientDetails = new PatientDetails("p1", studyDetails);

		List<PatientDetails> patientDetailsList = new ArrayList<PatientDetails>();
		patientDetailsList.add(patientDetails);

	    List<PatientDetailGroupWrapper> wrapperList =
	    	DailyReportUtil.convert(patientDetailsList);

	    assertEquals(wrapperList.size(),1);
	    PatientDetailGroupWrapper wrapper = wrapperList.get(0);
	    assertEquals(wrapper.getPatient(), "p1");
	    assertEquals(wrapper.getStudy(), "2");
	    assertEquals(wrapper.getSeries(), "6");
	    assertEquals(wrapper.getImg(), "20");

	    List<PatientDetailGroupWrapper> studyDetailsList = wrapper.getChildren();
	    assertEquals(studyDetailsList.size(),2);
	    PatientDetailGroupWrapper study1 = studyDetailsList.get(0);
	    assertEquals(study1.getPatient(), "");
	    assertEquals(study1.getStudy(), "studyUid1");
	    assertEquals(study1.getSeries(), "3");
	    assertEquals(study1.getImg(), "10");

	    List<PatientDetailGroupWrapper> seriesDetailsList = study1.getChildren();
	    assertEquals(seriesDetailsList.size(),3);
	    PatientDetailGroupWrapper series1 = seriesDetailsList.get(0);
	    assertEquals(series1.getPatient(), "");
	    assertEquals(series1.getStudy(), "");
	    assertEquals(series1.getSeries(), "s1");
	    assertEquals(series1.getImg(), "5");

	}

	public void testSelectDateDetailsNotEmpty() throws Exception {
		PatientDetailGroupWrapper pdgw1 = createPatientDetailGroupWrapper();

		List<PatientDetailGroupWrapper> list = new ArrayList<PatientDetailGroupWrapper>();
		list.add(pdgw1);

		DailyReportWrapper dailyReportWrapper = new DailyReportWrapper(list, 23);
		dailyReportWrapper.setExpanded(true);

		Map<String,DailyReportWrapper> dailyReportMap =
			new HashMap<String,DailyReportWrapper>();

		dailyReportMap.put("02-01-1996", dailyReportWrapper);
        String dayString = "02-01-1996";
        DailyReportWrapperFactory reportFactory = new DailyReportWrapperFactory() {
            public DailyReportWrapper create(String dateString, int submissionCount) throws Exception {
            	return new DailyReportWrapper(null, 666);
            }
        };

        DailyReportUtil.selectDateDetails(dailyReportMap, dayString, reportFactory);

        assertTrue(!dailyReportMap.get("02-01-1996").isExpanded());
	}

	public void testSelectDateDetailsEmpty() throws Exception {
		DailyReportWrapper dailyReportWrapper = new DailyReportWrapper(null, 23);

		Map<String,DailyReportWrapper> dailyReportMap =
			new HashMap<String,DailyReportWrapper>();

        String dayString = "02-01-1996";
		dailyReportMap.put(dayString, dailyReportWrapper);
        DailyReportWrapperFactory reportFactory = new DailyReportWrapperFactory() {
            public DailyReportWrapper create(String dateString, int submissionCount) throws Exception {
            	return new DailyReportWrapper(new ArrayList<PatientDetailGroupWrapper>(), 666);
            }
        };

        DailyReportUtil.selectDateDetails(dailyReportMap, dayString, reportFactory);

        DailyReportWrapper newWrapper = dailyReportMap.get(dayString);
        assertTrue(newWrapper.isExpanded());
        assertTrue(newWrapper.getSubmissionCount()==666);
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
