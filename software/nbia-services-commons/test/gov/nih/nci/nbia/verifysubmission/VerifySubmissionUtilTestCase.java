/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import gov.nih.nci.nbia.util.SiteData;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;

public class VerifySubmissionUtilTestCase extends TestCase {

	public void testSiteDataToString() {
        //just to appease coverage
        new VerifySubmissionUtil();

		SiteData siteData = new SiteData("foo//moo");
		assertTrue(VerifySubmissionUtil.siteDataToString(siteData).equals("foo//moo"));
	}

	public void testDateFormat() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2005, 2, 4);
		String formatted = VerifySubmissionUtil.dateFormat(calendar.getTime());
		System.out.println("formatted:"+formatted);
		assertTrue(formatted.equals("03/04/2005"));
	}

	public void testDateParse() throws Exception {
		Date date02_31_2002 = VerifySubmissionUtil.dateParse("02/26/2002");
		System.out.println("date02_31_2002:"+date02_31_2002);
		assertTrue(date02_31_2002.getMonth()==1);
		assertTrue(date02_31_2002.getYear()==102);
		assertTrue(date02_31_2002.getDate()==26);

		try {
			VerifySubmissionUtil.dateParse("screwup");
			fail("dateParse should have thrown Exception");
		}
		catch(Exception ex) {
			//ok
		}
	}

	public void testGetProject() {
		String projectSite = "Foo//moo";

		String project = VerifySubmissionUtil.getProject(projectSite);
		assertTrue(project.equals("Foo"));

		try {
			VerifySubmissionUtil.getProject("screwup");
			fail("should have bailed");
		}
		catch(Exception ex) {
			//ok
		}
	}

	public void testGetSite() {
		String projectSite = "Foo//moo";

		String project = VerifySubmissionUtil.getSite(projectSite);
		assertTrue(project.equals("moo"));

		try {
			VerifySubmissionUtil.getProject("screwup");
			fail("should have bailed");
		}
		catch(Exception ex) {
			//ok
		}
	}
}
