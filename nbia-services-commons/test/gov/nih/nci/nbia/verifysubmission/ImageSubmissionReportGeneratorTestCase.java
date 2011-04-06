package gov.nih.nci.nbia.verifysubmission;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class ImageSubmissionReportGeneratorTestCase extends AbstractDbUnitTestForJunit4  {
	@Test
	public void testGenerateAffectedReportByDay() throws Exception {
        Calendar day = new GregorianCalendar();
        day.set(2007,7,4);
        
		List<PatientDetails> patientDetailsList =
			imageSubmissionReportGenerator.generateAffectedReportByDay(day.getTime(), 
					                                                   "p2",
					                                                   "s2");
		Assert.assertEquals(patientDetailsList.size(), 4);
		
		Map<String, PatientDetails> map = new HashMap<String, PatientDetails>();
		for(PatientDetails pd : patientDetailsList) {
			System.out.println("pd.getPatientId():"+pd.getPatientId());
			map.put(pd.getPatientId(), pd);
		}
		
		PatientDetails pd1_2 = map.get("1.2");
		testPatientDetails(pd1_2, 1, 1, 1);
		
		List<StudyDetails> p1_2StudyDetails = pd1_2.getStudyDetails();
		testStudyDetails(p1_2StudyDetails.get(0), "2.4", 1, 1);
		
		List<SeriesDetails> p1_2seriesDetails = p1_2StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(p1_2seriesDetails.get(0), "3.4", 1);
		
////////////////////////
		PatientDetails pd1_3 = map.get("1.3");
		testPatientDetails(pd1_3, 1, 1, 1);
		
		List<StudyDetails> p1_3StudyDetails = pd1_3.getStudyDetails();
		testStudyDetails(p1_3StudyDetails.get(0), "2.5", 1, 1);
		
		List<SeriesDetails> p1_3seriesDetails = p1_3StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(p1_3seriesDetails.get(0), "3.5", 1);		
		
////////////////////////
		PatientDetails pd1_4 = map.get("1.4");
		testPatientDetails(pd1_4, 1, 1, 1);
		
		List<StudyDetails> pd1_4StudyDetails = pd1_4.getStudyDetails();
		testStudyDetails(pd1_4StudyDetails.get(0), "2.6", 1, 1);
		
		List<SeriesDetails> pd1_4seriesDetails = pd1_4StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(pd1_4seriesDetails.get(0), "3.6", 1);
		
////////////////////////
		PatientDetails pd1_5= map.get("1.5");
		testPatientDetails(pd1_5, 1, 1, 1);
		
		List<StudyDetails> pd1_5StudyDetails = pd1_5.getStudyDetails();
		testStudyDetails(pd1_5StudyDetails.get(0), "2.7", 1, 1);
		
		List<SeriesDetails> pd1_5seriesDetails = pd1_5StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(pd1_5seriesDetails.get(0), "3.7", 1);			
		
	}
	@Test
	public void testGenerateCorrectedReportByDay() throws Exception {
		Calendar day = new GregorianCalendar();
        day.set(2007,7,4);
        
		List<PatientDetails> patientDetailsList =
			imageSubmissionReportGenerator.generateCorrectedReportByDay(day.getTime(), 
					                                                   "p3",
					                                                   "s3");
		Assert.assertEquals(patientDetailsList.size(), 2);
			
		Map<String, PatientDetails> map = new HashMap<String, PatientDetails>();
		for(PatientDetails pd : patientDetailsList) {
			System.out.println("pd.getPatientId():"+pd.getPatientId());
			map.put(pd.getPatientId(), pd);
		}
		
		PatientDetails pd = map.get("1.8");
		Assert.assertEquals(pd.getPatientId(),"1.8");
		testPatientDetails(pd, 1, 1, 2);
		
		List<StudyDetails> studyDetails = pd.getStudyDetails();
		testStudyDetails(studyDetails.get(0), "2.10", 1, 2);
		
		List<SeriesDetails> seriesDetails = studyDetails.get(0).getSeriesDetails();
		testSeriesDetails(seriesDetails.get(0), "3.10", 2);
		
///////////////		
		pd = map.get("1.9");
		Assert.assertEquals(pd.getPatientId(),"1.9");
		testPatientDetails(pd, 1, 1, 1);
		
		studyDetails = pd.getStudyDetails();
		testStudyDetails(studyDetails.get(0), "2.11", 1, 1);
		
		seriesDetails = studyDetails.get(0).getSeriesDetails();
		testSeriesDetails(seriesDetails.get(0), "3.11", 1);		
	}
	@Test
	public void testGenerateReport() {
		Calendar startDate = new GregorianCalendar();
		startDate.set(2007,7,1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(2007,11,31);		
        
		ImageSubmissionReport imageSubmissionReport = 
			imageSubmissionReportGenerator.generateReport(startDate.getTime(),
                                                          endDate.getTime(),
                                                          "p2",
                                                          "s2");
		
		ImageSubmissionCountReport imageSubmissionCountReport = imageSubmissionReport.getImageSubmissionCountReport();
		Assert.assertNotNull(imageSubmissionCountReport);
		Assert.assertEquals(imageSubmissionCountReport.getAffectedPatientCount(),6); 
		Assert.assertEquals(imageSubmissionCountReport.getAffectedStudyCount(),6); 
		Assert.assertEquals(imageSubmissionCountReport.getAffectedSeriesCount(),6); 
		
		Assert.assertEquals(imageSubmissionCountReport.getCorrectedPatientCount(),1);
		Assert.assertEquals(imageSubmissionCountReport.getCorrectedStudyCount(),1); 
		Assert.assertEquals(imageSubmissionCountReport.getCorrectedSeriesCount(),1); 		
		Assert.assertEquals(imageSubmissionCountReport.getCorrectedImageCount(),1); 
				
		Map<Date,Integer> submissionDays = imageSubmissionReport.getSubmissionDays();
		List<String> submissionDaysStringSet = new ArrayList<String>();
		for(Date d : submissionDays.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);				
			submissionDaysStringSet.add(dStr);
		}
		Assert.assertEquals(submissionDaysStringSet.size(), 5);
		Assert.assertTrue(submissionDaysStringSet.contains("2007-08-04"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-08-05"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-11-04"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-11-05"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-11-06"));

		
		Map<Date,Integer> correctedSubmissionDays = imageSubmissionReport.getCorrectedSubmissionDays();
		List<String> correctedSubmissionDaysStringSet = new ArrayList<String>();
		for(Date d : correctedSubmissionDays.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
	    	correctedSubmissionDaysStringSet.add(dStr);
		}
		Assert.assertEquals(correctedSubmissionDaysStringSet.size(), 1);
		Assert.assertTrue(correctedSubmissionDaysStringSet.contains("2007-08-04"));
		
	
	}
	
	
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    @Before
	public void setUp() throws Exception {
		imageSubmissionReportGenerator = new ImageSubmissionReportGenerator();
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/submission_history.xml";

	private ImageSubmissionReportGenerator imageSubmissionReportGenerator;	

	private static void testPatientDetails(PatientDetails pd,
			                               int patientStudyCount,
			                               int pateintSeriesCount,
			                               int patientSubmissionCount) throws Exception {
		Assert.assertNotNull(pd);
		Assert.assertEquals(pd.getStudyCount(),patientStudyCount);
		Assert.assertEquals(pd.getSeriesCount(),pateintSeriesCount);
		Assert.assertEquals(pd.getSubmissionCount(),patientSubmissionCount);
		
		List<StudyDetails> studyDetailsList = pd.getStudyDetails();
		Assert.assertNotNull(studyDetailsList);
		Assert.assertEquals(studyDetailsList.size(), patientStudyCount);			
	}
	
	private static void testStudyDetails(StudyDetails sd, 
			                             String studyInstanceUid,
			                             int studySeriesCount, 
			                             int studySubmissionCount) throws Exception {	

		Assert.assertEquals(sd.getSeriesCount(),studySeriesCount);
		Assert.assertEquals(sd.getSubmissionCount(),studySubmissionCount);
		Assert.assertEquals(sd.getStudyInstanceUid(),studyInstanceUid);
		
		List<SeriesDetails> seriesDetailsList = sd.getSeriesDetails();
		Assert.assertNotNull(seriesDetailsList);		
		Assert.assertEquals(seriesDetailsList.size(),studySeriesCount);
	}
	
	private static void testSeriesDetails(SeriesDetails sd, 
                                          String seriesInstanceUid,
                                          int submissionCount) throws Exception {	
		Assert.assertEquals(sd.getSeriesInstanceUid(),seriesInstanceUid);
		Assert.assertEquals(sd.getSubmissionCount(),submissionCount);		
	}
}
