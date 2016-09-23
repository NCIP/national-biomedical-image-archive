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
import gov.nih.nci.nbia.dto.SeriesSubmissionCountDTO;
import gov.nih.nci.nbia.dto.SubmissionCountsDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringApplicationContext.class}) 
public class AnnotationReportGeneratorTestCase  {

	@Test
	public void testGenerateReportByDayExistingSeries() {
		
		Calendar cal = new GregorianCalendar();
		cal.set(2007,7,5);
        
		SeriesSubmissionCountDTO dto0 = new SeriesSubmissionCountDTO("1.10", 
				                                                     "2.12",
				                                                     "3.12", 
                                                                     1);
		
		SeriesSubmissionCountDTO dto1 = new SeriesSubmissionCountDTO("1.10", 
                                                                     "2.12",
                                                                     "3.13",
                                                                     1);		
		
		List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs =
			new ArrayList<SeriesSubmissionCountDTO>();
		seriesSubmissionCountDTOs.add(dto0);
		seriesSubmissionCountDTOs.add(dto1);

        expect(submissionHistoryDAOMock.findSeriesSubmissionCountInTimeFrame(cal.getTime(),
        		                                                             cal.getTime(), 
        		                                                             "p4", 
        		                                                             "s4", 
        		                                                             /*opType*/2)).
            andReturn(seriesSubmissionCountDTOs);        		                                                             
        

        
        replayMocks();
        
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
        
		
		SubmissionCountsDTO submissionCountsDTO = new SubmissionCountsDTO(1,1,2,2);


        expect(submissionHistoryDAOMock.findAnnotationCounts(startDate.getTime(),
        		                                             endDate.getTime(),
        		                                             "p4",
        		                                             "s4")).
            andReturn(submissionCountsDTO);  		
		
        
        DayCountDTO dto0 = new DayCountDTO(new Date(107,7,5), 2);
        
        List<DayCountDTO> dayCountList = new ArrayList<DayCountDTO>();
        dayCountList.add(dto0);
        
        expect(submissionHistoryDAOMock.findSubmissionDatesInTimeFrame(eq(startDate.getTime()),
        		                                                       eq(endDate.getTime()),
        		                                                       eq("p4"),
        		                                                       eq("s4"),
        		                                                       aryEq(new Integer[]{SubmissionHistoryDAO.ANNOTATION_SUBMISSION_OPERATION}))).
           andReturn(dayCountList);  	
        
        replayMocks();
        
		/////////////////////////////////////////////////////
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


    @Before
	public void setUp() throws Exception {
        mockStatic(SpringApplicationContext.class);
		submissionHistoryDAOMock = createMock(SubmissionHistoryDAO.class);
		
        expect(SpringApplicationContext.getBean("submissionHistoryDAO")).
        andReturn(submissionHistoryDAOMock);	
        
		
		annotationReportGenerator = new AnnotationReportGenerator();        
	}
	
    @After
    public void tearDown() {
    	verifyMocks();
    }
    
    ////////////////////////////////////PRIVATE/////////////////////////////////

	private SubmissionHistoryDAO submissionHistoryDAOMock;

	private AnnotationReportGenerator annotationReportGenerator;	
	
	private void replayMocks() {
        replay(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        replay(SpringApplicationContext.class);		
	}
	
	private void verifyMocks() {
        verify(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        verify(SpringApplicationContext.class);			
	}
}
