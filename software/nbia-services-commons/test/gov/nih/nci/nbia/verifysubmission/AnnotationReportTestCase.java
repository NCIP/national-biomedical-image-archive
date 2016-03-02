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

public class AnnotationReportTestCase extends TestCase {

	public void testGetSubmissionDays() {
		AnnotationReport annotationReport = constructReport();
		
		SortedMap<Date,Integer> submissionDays = annotationReport.getSubmissionDays();
		assertTrue(submissionDays.size()==2);
		try {
			submissionDays.put(new Date(), 2);
			fail("should be unmodifiable");
		}
		catch(Exception ex) {
			//ok
		}
		
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5,0,0,0);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6,0,0,0);
        
		assertTrue(annotationReport.getAnnotationCountReport()!=null);
		
		assertTrue(annotationReport.getCollectionSite().equals("fakePrj//fakeSite"));

		assertEquals(VerifySubmissionUtil.dateFormat(annotationReport.getFrom()),
				     "08/05/2007");
		assertEquals(VerifySubmissionUtil.dateFormat(annotationReport.getTo()),
	                 "11/06/2007");
	}
	
	private static AnnotationReport constructReport() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 2);
	
		AnnotationCountReport annotationCountReport =
			new AnnotationCountReport(1,2,3,4);
        
		SortedMap<Date, Integer> submissionDays = new TreeMap<Date, Integer>();
        submissionDays.put(new Date(), 2);
        submissionDays.put(c.getTime(), 3);
        
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5,0,0,0);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6,0,0,0);
        
        AnnotationReport annotationReport = 
			new AnnotationReport(annotationCountReport,
					             submissionDays,
					             start.getTime(),
					             end.getTime(),
					             "fakePrj//fakeSite");
		return annotationReport;
	}
}
