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

public class AccrualReportTestCase extends TestCase {

	public void testAccessors() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 2);

		SortedMap<Date, Integer> days = new TreeMap<Date, Integer>();
        days.put(new Date(), 2);
        days.put(c.getTime(), 5);
        
        Calendar start = new GregorianCalendar();
        start.set(2007,7,5);
        
        Calendar end = new GregorianCalendar();
        end.set(2007,10,6);
        
		AccrualReport accrualReport =
			new AccrualReport(0,1,2,3,4,5,6,7,8,9,10,days,start.getTime(),end.getTime(),"fakePrj//fakeSite");
		
		assertTrue(accrualReport.getNewPatientCount()==0);
		assertTrue(accrualReport.getUpdatedPatientCount()==1);
		assertTrue(accrualReport.getCorrectedPatientCount()==2);
		assertTrue(accrualReport.getNewStudyCount()==3);
		assertTrue(accrualReport.getUpdatedStudyCount()==4);		
		assertTrue(accrualReport.getCorrectedStudyCount()==5);
		assertTrue(accrualReport.getNewSeriesCount()==6);
		assertTrue(accrualReport.getUpdatedSeriesCount()==7);
		assertTrue(accrualReport.getCorrectedSeriesCount()==8);
		assertTrue(accrualReport.getCorrectedImageCount()==9);
		assertTrue(accrualReport.getNewImageCount()==10);
		assertTrue(accrualReport.getCollectionSite().equals("fakePrj//fakeSite"));

		assertTrue(accrualReport.getFrom().equals(start.getTime()));
		assertTrue(accrualReport.getTo().equals(end.getTime()));

		assertTrue(accrualReport.getAccrualDays().size()==2);		
		try {
			accrualReport.getAccrualDays().put(new Date(), 33);
			fail("should be unmodifiable");
		}
		catch(Exception ex) {
			//ok
		}
	}
}
