package gov.nih.nci.ncia.verifysubmission;

import gov.nih.nci.nbia.verifysubmission.AccrualReport;
import gov.nih.nci.nbia.verifysubmission.AccrualReportGenerator;
import gov.nih.nci.ncia.AbstractDbUnitTestForJunit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-service.xml", "/applicationContext-hibernate-testContext.xml"})
public class AccrualReportGeneratorTestCase extends AbstractDbUnitTestForJunit4  {

	@Test
	public void testGenerateReportByDay() {
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);
        
        AccrualReport accrualReport = accrualReportGenerator.generateReportByDay(start.getTime(), "p1", "s1");
		
		SortedMap<Date, Integer> days = accrualReport.getAccrualDays();
		List<String> dayStringSet = new ArrayList<String>();
		for(Date d : days.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			dayStringSet.add(dStr);
		}
		Assert.assertEquals(days.size(), 1);
		
		Assert.assertTrue(dayStringSet.contains("2006-08-04"));

		
		Assert.assertEquals(accrualReport.getNewPatientCount(),1);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),0);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),0);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),0);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),0);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),4);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),0);		
	}

	@Test
	public void testGenerateReportForNews() {
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);
        
		AccrualReport accrualReport = accrualReportGenerator.generateReport(start.getTime(), 
				                                                            end.getTime(), 
				                                                            "p2", 
				                                                            "s2");
		
		SortedMap<Date, Integer> days = accrualReport.getAccrualDays();
		List<String> dayStringSet = new ArrayList<String>();
		for(Date d : days.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			dayStringSet.add(dStr);
		}
		Assert.assertEquals(days.size(), 4);
		Assert.assertTrue(dayStringSet.contains("2007-08-05"));
		Assert.assertTrue(dayStringSet.contains("2007-11-04"));
		Assert.assertTrue(dayStringSet.contains("2007-11-05"));
		Assert.assertTrue(dayStringSet.contains("2007-11-06"));
		
		Assert.assertEquals(accrualReport.getNewPatientCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),0);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),0);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),0);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),0);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),7);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),0);
	}
	
	@Test
	public void testGenerateReportForUpdates() {
        Calendar start = new GregorianCalendar();
        start.set(2007,10,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);
        
		AccrualReport accrualReport = accrualReportGenerator.generateReport(start.getTime(), 
				                                                            end.getTime(), 
				                                                            "p2", 
				                                                            "s2");
		
		SortedMap<Date, Integer> days = accrualReport.getAccrualDays();
		List<String> dayStringSet = new ArrayList<String>();
		for(Date d : days.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			dayStringSet.add(dStr);
		}
		Assert.assertEquals(days.size(), 2);
		Assert.assertTrue(dayStringSet.contains("2007-11-05"));
		Assert.assertTrue(dayStringSet.contains("2007-11-06"));
		
		Assert.assertEquals(accrualReport.getNewPatientCount(),0);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),1);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),0);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),0);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),1);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),0);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),0);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),1);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),0);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),4);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),0);
	}	
	
	@Test
	public void testGenerateReportForCorrections() {
        Calendar start = new GregorianCalendar();
        start.set(2007,7,4);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,7,5);
        
		AccrualReport accrualReport = accrualReportGenerator.generateReport(start.getTime(), 
				                                                            end.getTime(), 
				                                                            "p3", 
				                                                            "s3");
		
		SortedMap<Date, Integer> days = accrualReport.getAccrualDays();
		List<String> dayStringSet = new ArrayList<String>();
		for(Date d : days.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			dayStringSet.add(dStr);
		}
		Assert.assertEquals(days.size(), 2);
		Assert.assertTrue(dayStringSet.contains("2007-08-04"));
		Assert.assertTrue(dayStringSet.contains("2007-08-05"));
		
		Assert.assertEquals(accrualReport.getNewPatientCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),2);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),0);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),2);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),2);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),0);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),2);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),3);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),3);
	}	
    //////////////////////////////PROTECTED/////////////////////////////////

    protected String getDataSetResourceSpec() {
    	return TEST_DB_FLAT_FILE;
    }

    @Before
	public void setUp() throws Exception {
		accrualReportGenerator = new AccrualReportGenerator();
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private static final String TEST_DB_FLAT_FILE = "dbunitscripts/submission_history.xml";

	private AccrualReportGenerator accrualReportGenerator;
}
