/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dao.SubmissionHistoryDAO;
import gov.nih.nci.nbia.dto.DayCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringApplicationContext.class}) 
public class AccrualReportGeneratorTestCase   {

	@Test
	public void testGenerateReportNoEndDate() {
		
        mockStatic(SpringApplicationContext.class);
		submissionHistoryDAOMock = createMock(SubmissionHistoryDAO.class);
		
        expect(SpringApplicationContext.getBean("submissionHistoryDAO")).
            andReturn(submissionHistoryDAOMock);
	
		////////////////////////////////////////////////////////////////////
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);
        
        int newPatientCount = 4;
        int updatedPatientCount = 2;
        int correctedPatientCount = 1;
        
        int newStudyCount = 34;
        int updatedStudyCount = 22;
        int correctedStudyCount = 15;
        
        int newSeriesCount = 67;
        int updatedSeriesCount = 45;
        int correctedSeriesCount = 33;
        
        int newImageCount = 234;
        int correctedImageCount = 57;
        
        expectAccrual(start.getTime(), 
        		      start.getTime(), 
        		      "p1", 
        		      "s1",
        		
        		      newPatientCount,
        		      updatedPatientCount,
        		      correctedPatientCount,      
        
        		      newStudyCount,
        		      updatedStudyCount,
        		      correctedStudyCount,
        		      
        		      newSeriesCount,
        		      updatedSeriesCount,
        		      correctedSeriesCount,
        		      
        		      newImageCount,
        		      correctedImageCount);
        
        replay(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        replay(SpringApplicationContext.class);
        
        AccrualReport accrualReport = accrualReportGenerator.generateReportByDay(start.getTime(), 
        		                                                                 "p1", 
        		                                                                 "s1");
		
		SortedMap<Date, Integer> days = accrualReport.getAccrualDays();
		List<String> dayStringSet = new ArrayList<String>();
		for(Date d : days.keySet()) {
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	String dStr = sdf.format(d);			
			dayStringSet.add(dStr);
			System.out.println("foo:"+dStr);

		}
		Assert.assertEquals(days.size(), 2);
		
		Assert.assertTrue(dayStringSet.contains("2006-08-04"));
		Assert.assertTrue(dayStringSet.contains("1974-03-02"));

		
		Assert.assertEquals(accrualReport.getNewPatientCount(),newPatientCount);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),updatedPatientCount);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),correctedPatientCount);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),newStudyCount);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),updatedStudyCount);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),correctedStudyCount);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),newSeriesCount);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),updatedSeriesCount);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),correctedSeriesCount);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),newImageCount);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),correctedImageCount);
		
        verify(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        verify(SpringApplicationContext.class);		
		
	}

	@Test
	public void testGenerateReportWithEndDate() {
        mockStatic(SpringApplicationContext.class);
		submissionHistoryDAOMock = createMock(SubmissionHistoryDAO.class);
		
        expect(SpringApplicationContext.getBean("submissionHistoryDAO")).
            andReturn(submissionHistoryDAOMock);
	
		////////////////////////////////////////////////////////////////////
        
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);
        
        int newPatientCount = 4;
        int updatedPatientCount = 2;
        int correctedPatientCount = 1;
        
        int newStudyCount = 34;
        int updatedStudyCount = 22;
        int correctedStudyCount = 15;
        
        int newSeriesCount = 67;
        int updatedSeriesCount = 45;
        int correctedSeriesCount = 33;
        
        int newImageCount = 234;
        int correctedImageCount = 57;
        
        expectAccrual(start.getTime(), 
        		      end.getTime(), 
        		      "p2", 
        		      "s2",
        		
        		      newPatientCount,
        		      updatedPatientCount,
        		      correctedPatientCount,      
        
        		      newStudyCount,
        		      updatedStudyCount,
        		      correctedStudyCount,
        		      
        		      newSeriesCount,
        		      updatedSeriesCount,
        		      correctedSeriesCount,
        		      
        		      newImageCount,
        		      correctedImageCount);        
        
        replay(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        replay(SpringApplicationContext.class);
        
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
			System.out.println("foo:"+dStr);
		}
		Assert.assertEquals(days.size(), 2);
			
		Assert.assertTrue(dayStringSet.contains("2007-08-05"));
		Assert.assertTrue(dayStringSet.contains("1974-03-02"));
		
		Assert.assertEquals(accrualReport.getNewPatientCount(),newPatientCount);
		Assert.assertEquals(accrualReport.getUpdatedPatientCount(),updatedPatientCount);
		Assert.assertEquals(accrualReport.getCorrectedPatientCount(),correctedPatientCount);
		
		Assert.assertEquals(accrualReport.getNewStudyCount(),newStudyCount);
		Assert.assertEquals(accrualReport.getUpdatedStudyCount(),updatedStudyCount);
		Assert.assertEquals(accrualReport.getCorrectedStudyCount(),correctedStudyCount);

		Assert.assertEquals(accrualReport.getNewSeriesCount(),newSeriesCount);
		Assert.assertEquals(accrualReport.getUpdatedSeriesCount(),updatedSeriesCount);	
		Assert.assertEquals(accrualReport.getCorrectedSeriesCount(),correctedSeriesCount);

		
		Assert.assertEquals(accrualReport.getNewImageCount(),newImageCount);
		Assert.assertEquals(accrualReport.getCorrectedImageCount(),correctedImageCount);

        verify(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        verify(SpringApplicationContext.class);			
	}

    @Before
	public void setUp() throws Exception {
		accrualReportGenerator = new AccrualReportGenerator();
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

	private AccrualReportGenerator accrualReportGenerator;
	
	private SubmissionHistoryDAO submissionHistoryDAOMock;

	private void expectAccrual(Date startDate, 
			                   Date endDate, 
			                   String projectName, 
			                   String siteName,
			                   
			                   int newPatientCount,
			                   int updatedPatientCount,
			                   long correctedPatientCount,
			                   
			                   int newStudyCount,
			                   int updatedStudyCount,
			                   long correctedStudyCount,
			                   
			                   int newSeriesCount,
			                   int updatedSeriesCount,
			                   long correctedSeriesCount,
			                   
			                   long newImageCount,
			                   long correctedImageCount) {
		
		SubmissionCountsDTO correctedCountsDTO = new SubmissionCountsDTO(correctedPatientCount,
				                                                         correctedStudyCount,
				                                                         correctedSeriesCount,
				                                                         correctedImageCount);
		expect(submissionHistoryDAOMock.findCorrectedCounts(startDate, 
				                                            endDate,
				                                            projectName,
				                                            siteName)).
	        andReturn(correctedCountsDTO);
		
		expect(submissionHistoryDAOMock.findNewPatientCountInTimeFrame(startDate, 
                                                                       endDate,
                                                                       projectName,
                                                                       siteName)).
            andReturn(newPatientCount);				                                         
        
		expect(submissionHistoryDAOMock.findNewStudyCountInTimeFrame(startDate, 
                                                                     endDate,
                                                                     projectName,
                                                                     siteName)).
            andReturn(newStudyCount);	

		expect(submissionHistoryDAOMock.findNewSeriesCountInTimeFrame(startDate, 
                                                                      endDate,
                                                                      projectName,
                                                                      siteName)).
            andReturn(newSeriesCount);	
        
		expect(submissionHistoryDAOMock.findUpdatedPatientCountInTimeFrame(startDate,
				                                                           endDate,
				                                                           projectName,
				                                                           siteName)).
            andReturn(updatedPatientCount);			

		expect(submissionHistoryDAOMock.findUpdatedStudyCountInTimeFrame(startDate,
                                                                         endDate,
                                                                         projectName,
                                                                         siteName)).
            andReturn(updatedStudyCount);	        

        
		expect(submissionHistoryDAOMock.findUpdatedSeriesCountInTimeFrame(startDate,
                                                                          endDate,
                                                                          projectName,
                                                                          siteName)).
            andReturn(updatedSeriesCount);	 
        
		expect(submissionHistoryDAOMock.findImageSubmissionCountInTimeFrame(startDate,
                                                                            endDate,
                                                                            projectName,
                                                                            siteName)).
            andReturn(newImageCount);		

		
		List<DayCountDTO> submissionDays = new ArrayList<DayCountDTO>();
		
		
		
		DayCountDTO dto0 = new DayCountDTO(startDate, 254);
		submissionDays.add(dto0);
		DayCountDTO dto1 = new DayCountDTO(new Date(74,2,2), 254);
		submissionDays.add(dto1);		
       

        Integer[] flags = new Integer[] {SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION,
                                         SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION};
		expect(submissionHistoryDAOMock.findSubmissionDatesInTimeFrame(eq(startDate),
                                                                       eq(endDate),
                                                                       eq(projectName),
                                                                       eq(siteName),
                                                                       aryEq(flags))).
            andReturn(submissionDays);			
	}
}
