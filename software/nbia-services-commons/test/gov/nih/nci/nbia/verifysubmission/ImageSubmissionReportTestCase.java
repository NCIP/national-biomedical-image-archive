/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.TestCase;

public class ImageSubmissionReportTestCase extends TestCase {

	public void testGetSubmissionDays() {

		ImageSubmissionReport imageSubmissionReport = constructReport();
		
		SortedMap<Date, Integer> submissionDays = imageSubmissionReport.getSubmissionDays();
		assertTrue(submissionDays.size()==2);
		try {
			submissionDays.put(new Date(),1);
			fail("should be unmodifiable");
		}
		catch(Exception ex) {
			//ok
		}
		
		SortedMap<Date,Integer> correctedSubmissionDays = imageSubmissionReport.getCorrectedSubmissionDays();
		assertTrue(correctedSubmissionDays.size()==2);
		try {
			correctedSubmissionDays.put(new Date(),2);
			fail("should be unmodifiable");
		}
		catch(Exception ex) {
			//ok
		}		
		
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);		
		
		assertTrue(imageSubmissionReport.getImageSubmissionCountReport()!=null);
		assertTrue(imageSubmissionReport.getCollectionSite().equals("fakePrj//fakeSite"));

		assertEquals(VerifySubmissionUtil.dateFormat(imageSubmissionReport.getFrom()),
	                 "08/05/2007");
        assertEquals(VerifySubmissionUtil.dateFormat(imageSubmissionReport.getTo()),
                     "11/06/2007");					
	}
	
	private static ImageSubmissionReport constructReport() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 2);
	
		ImageSubmissionCountReport imageSubmissionCountReport =
			new ImageSubmissionCountReport(1,2,3,4,5,6,7,8);
        
		SortedMap<Date, Integer> submissionDays = new TreeMap<Date, Integer>();
        submissionDays.put(new Date(),4);
        submissionDays.put(c.getTime(),5);
        
        SortedMap<Date, Integer> correctedSubmissionDays = new TreeMap<Date,Integer>();
		correctedSubmissionDays.put(new Date(),2);
		correctedSubmissionDays.put(c.getTime(),3);
	
		
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);
        
		ImageSubmissionReport imageSubmissionReport = 
			new ImageSubmissionReport(imageSubmissionCountReport,
					                  submissionDays,
					                  correctedSubmissionDays,
					                  start.getTime(),
					                  end.getTime(),
					                  "fakePrj//fakeSite");
		return imageSubmissionReport;
	}

}
