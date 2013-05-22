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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
public class ImageSubmissionReportGeneratorTestCase {
	
	@Test
	public void testGenerateAffectedReportByDay() throws Exception {
		this.testByDay(SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION);
	}
	
    @Test
	public void testGenerateCorrectedReportByDay() throws Exception {
	    this.testByDay(SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION);  
    }
    	

	@Test
	public void testGenerateReport() throws Exception {
		Calendar startCal = new GregorianCalendar();
		startCal.set(2007,7,1);
		
		Calendar endCal = new GregorianCalendar();
		endCal.set(2007,11,31);	
			
	    Date startDate = startCal.getTime();
	    Date endDate = endCal.getTime();
	    String projectName = "p2";
	    String siteName = "s2";
	    
	    SubmissionCountsDTO imageSubmissionCountsDTO = new SubmissionCountsDTO(2,3,4,6);
	    expect(submissionHistoryDAOMock.findImageCounts(startDate,
                                                        endDate,
                                                        projectName,
                                                        siteName)).
            andReturn(imageSubmissionCountsDTO);                                                

        SubmissionCountsDTO correctedCountsDTO =new SubmissionCountsDTO(1,1,1,1);
	    expect(submissionHistoryDAOMock.findCorrectedCounts(startDate,
                                                            endDate,
                                                            projectName,
                                                            siteName)).
            andReturn(correctedCountsDTO); 
            
        List<DayCountDTO> expectedSubmissionDays = new ArrayList<DayCountDTO>();
        DayCountDTO dto0 = new DayCountDTO(new Date(107,7,4),666);
        DayCountDTO dto1 = new DayCountDTO(new Date(107,7,5),666);
        DayCountDTO dto2 = new DayCountDTO(new Date(107,10,4),666);
        expectedSubmissionDays.add(dto0);
        expectedSubmissionDays.add(dto1); 
        expectedSubmissionDays.add(dto2); 
         
	    expect(submissionHistoryDAOMock.findSubmissionDatesInTimeFrame(eq(startDate),
                                                                       eq(endDate),
                                                                       eq(projectName),
                                                                       eq(siteName),
                                                                       aryEq(new Integer[]{SubmissionHistoryDAO.NEW_IMAGE_SUBMISSION_OPERATION}))).
            andReturn(expectedSubmissionDays); 
            
        List<DayCountDTO> expectedCorrectedSubmissionDays = new ArrayList<DayCountDTO>();            
        DayCountDTO cdto0 = new DayCountDTO(new Date(107,7,4),666);
        expectedCorrectedSubmissionDays.add(cdto0);    
	    expect(submissionHistoryDAOMock.findSubmissionDatesInTimeFrame(eq(startDate),
                                                                       eq(endDate),
                                                                       eq(projectName),
                                                                       eq(siteName),
                                                                       aryEq(new Integer[]{SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION}))).
            andReturn(expectedCorrectedSubmissionDays);             

        replayMocks();
        
		ImageSubmissionReport imageSubmissionReport = 
			imageSubmissionReportGenerator.generateReport(startDate,
                                                          endDate,
                                                          "p2",
                                                          "s2");
		
		ImageSubmissionCountReport imageSubmissionCountReport = imageSubmissionReport.getImageSubmissionCountReport();
		Assert.assertNotNull(imageSubmissionCountReport);
		Assert.assertEquals(imageSubmissionCountReport.getAffectedPatientCount(),2); 
		Assert.assertEquals(imageSubmissionCountReport.getAffectedStudyCount(),3); 
		Assert.assertEquals(imageSubmissionCountReport.getAffectedSeriesCount(),4); 
    	Assert.assertEquals(imageSubmissionCountReport.getSubmittedImageCount(),6); 
		
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
		Assert.assertEquals(submissionDaysStringSet.size(), 3);
		Assert.assertTrue(submissionDaysStringSet.contains("2007-08-04"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-08-05"));
		Assert.assertTrue(submissionDaysStringSet.contains("2007-11-04"));

		
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
	
    @Before
	public void setUp() throws Exception {
        mockStatic(SpringApplicationContext.class);
		submissionHistoryDAOMock = createMock(SubmissionHistoryDAO.class);
		
        expect(SpringApplicationContext.getBean("submissionHistoryDAO")).
        andReturn(submissionHistoryDAOMock);	
        
		imageSubmissionReportGenerator = new ImageSubmissionReportGenerator();
	}
	
    @After
    public void tearDown() {
    	verifyMocks();
    }


    ////////////////////////////////////PRIVATE/////////////////////////////////

	private SubmissionHistoryDAO submissionHistoryDAOMock;

	private ImageSubmissionReportGenerator imageSubmissionReportGenerator;	

	private void replayMocks() {
        replay(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        replay(SpringApplicationContext.class);		
	}
	
	private void verifyMocks() {
        verify(SubmissionHistoryDAO.class, submissionHistoryDAOMock);
        verify(SpringApplicationContext.class);			
	}

    private void testByDay(int operationType) throws Exception {
        Calendar day = new GregorianCalendar();
        day.set(2007,7,4);
        
        SeriesSubmissionCountDTO dto0 = new SeriesSubmissionCountDTO("1.2", 
        		                                                     "2.4", 
        		                                                     "3.4",
        		                                                     1);

        SeriesSubmissionCountDTO dto1 = new SeriesSubmissionCountDTO("1.3",
                                                                     "2.5",
                                                                     "3.5",
                                                                     1);

        SeriesSubmissionCountDTO dto2 = new SeriesSubmissionCountDTO("1.4",
                                                                     "2.6",
                                                                     "3.6",
                                                                     0);
                                                                     
        SeriesSubmissionCountDTO dto3 = new SeriesSubmissionCountDTO("1.5",
                                                                     "2.7",
                                                                     "3.7",
                                                                     5);
                                                                                                                                                  
        List<SeriesSubmissionCountDTO> seriesSubmissionCountDTOs =
        	new ArrayList<SeriesSubmissionCountDTO>();
        seriesSubmissionCountDTOs.add(dto0);
        seriesSubmissionCountDTOs.add(dto1);
        seriesSubmissionCountDTOs.add(dto2);
        seriesSubmissionCountDTOs.add(dto3);
                                
        expect(submissionHistoryDAOMock.findSeriesSubmissionCountInTimeFrame(day.getTime(),
        		                                                             day.getTime(),
        		                                                             "p2",
        		                                                             "s2",
        		                                                             operationType)).
           andReturn(seriesSubmissionCountDTOs);         
        
        replayMocks();
        
        
		List<PatientDetails> patientDetailsList = null;
		
		if(operationType==SubmissionHistoryDAO.REPLACE_IMAGE_SUBMISSION_OPERATION) {
		    patientDetailsList =
			imageSubmissionReportGenerator.generateCorrectedReportByDay(day.getTime(), 
					                                                    "p2",
					                                                    "s2");		
		}
		else {
		    patientDetailsList =
			imageSubmissionReportGenerator.generateAffectedReportByDay(day.getTime(), 
					                                                   "p2",
					                                                   "s2");
					                                                   
        }					                                                   
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
		testPatientDetails(pd1_4, 1, 1, 0);
		
		List<StudyDetails> pd1_4StudyDetails = pd1_4.getStudyDetails();
		testStudyDetails(pd1_4StudyDetails.get(0), "2.6", 1, 0);
		
		List<SeriesDetails> pd1_4seriesDetails = pd1_4StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(pd1_4seriesDetails.get(0), "3.6", 0);
		
////////////////////////
		PatientDetails pd1_5= map.get("1.5");
		testPatientDetails(pd1_5, 1, 1, 5);
		
		List<StudyDetails> pd1_5StudyDetails = pd1_5.getStudyDetails();
		testStudyDetails(pd1_5StudyDetails.get(0), "2.7", 1, 5);
		
		List<SeriesDetails> pd1_5seriesDetails = pd1_5StudyDetails.get(0).getSeriesDetails();
		testSeriesDetails(pd1_5seriesDetails.get(0), "3.7", 5);			
		
	}
	
	

    
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
