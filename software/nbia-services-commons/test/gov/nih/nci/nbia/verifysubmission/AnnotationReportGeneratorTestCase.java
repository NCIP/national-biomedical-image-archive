package gov.nih.nci.nbia.verifysubmission;

import gov.nih.nci.nbia.AbstractDbUnitTestForJunit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class AnnotationReportGeneratorTestCase extends AbstractDbUnitTestForJunit4 {

	@Test
	public void testGenerateReportByDayExistingSeries() {
		Calendar cal = new GregorianCalendar();
		cal.set(2007,7,5);
        
		List<PatientDetails> patientDetailsList = 
			annotationReportGenerator.generateReportByDay(cal.getTime(),
                                                          "p4",
                                                          "s4");
		
		Assert.assertEquals(patientDetailsList.size(),1);
		PatientDetails pd = patientDetailsList.get(0);
		Assert.assertEquals(pd.getPatientId(),"1.10");
		Assert.assertEquals(pd.getStudyCount(),1);
		Assert.assertEquals(pd.getSeriesCount(),2);
		Assert.assertEquals(pd.getSubmissionCount(),2);

		StudyDetails sd0 = pd.getStudyDetails().get(0);
		Assert.assertEquals(sd0.getStudyInstanceUid(), "2.12");
		Assert.assertEquals(sd0.getSeriesCount(), 2);
		Assert.assertEquals(sd0.getSubmissionCount(),2);
		
		List<SeriesDetails> sorted = new ArrayList<SeriesDetails>(sd0.getSeriesDetails());
		Collections.sort(sorted, new Comparator<SeriesDetails>() {
			public int compare(SeriesDetails o1, SeriesDetails o2) {
				return o1.getSeriesInstanceUid().compareTo(o2.getSeriesInstanceUid());			}
			
		});
		
		SeriesDetails ser0 = sorted.get(0);
		Assert.assertEquals(ser0.getSeriesInstanceUid(), "3.12");
		Assert.assertEquals(ser0.getSubmissionCount(),1);
		
		SeriesDetails ser1 = sorted.get(1);
		Assert.assertEquals(ser1.getSeriesInstanceUid(), "3.13");
		Assert.assertEquals(ser1.getSubmissionCount(),1);				
	}
	
	@Test	
	public void testGenerateReport() throws Exception {
		Calendar startDate = new GregorianCalendar();
		startDate.set(2007,7,1);
		
		Calendar endDate = new GregorianCalendar();
		endDate.set(2007,7,31);		
        
		AnnotationReport annotationReport = 
			annotationReportGenerator.generateReport(startDate.getTime(),
                                                     endDate.getTime(),
                                                     "p4",
                                                     "s4");
		
		AnnotationCountReport annotationCountReport = annotationReport.getAnnotationCountReport();
		Assert.assertNotNull(annotationCountReport);
		Assert.assertEquals(annotationCountReport.getAffectedPatientCount(),1); 
		Assert.assertEquals(annotationCountReport.getAffectedStudyCount(),1); 
		Assert.assertEquals(annotationCountReport.getAffectedSeriesCount(),2); 
		Assert.assertEquals(annotationCountReport.getAnnotationSubmissionCount(),2); 

				
		Map<Date,Integer> submissionDays = annotationReport.getSubmissionDays();
		List<String> submissionDaysStringSet = new ArrayList<String>();
		for(Date d : submissionDays.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			submissionDaysStringSet.add(dStr);
		}
		Assert.assertEquals(submissionDaysStringSet.size(), 1);
		
		Assert.assertTrue(submissionDaysStringSet.contains("2007-08-05"));

        Assert.assertTrue(submissionDays.get(VerifySubmissionUtil.dateParse("08/05/2007")).equals(2));
	}

    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    @Before
	public void setUp() throws Exception {

		annotationReportGenerator = new AnnotationReportGenerator();
	}
	
    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/submission_history.xml";

	private AnnotationReportGenerator annotationReportGenerator;		
}
