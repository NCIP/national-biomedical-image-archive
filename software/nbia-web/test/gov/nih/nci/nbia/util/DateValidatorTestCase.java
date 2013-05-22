/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class DateValidatorTestCase extends TestCase {

	public void testValidateFromDate() {
		String msg = dateValidator.validateDates(null, new Date(), true);
		assertEquals(msg, "A From Date must be included");
	}


	public void testValidateOrdering() {
        Calendar end = new GregorianCalendar();
        end.set(2006,7,1);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,4);

		String msg = dateValidator.validateDates(start.getTime(), end.getTime(), false);
		assertEquals(msg, "To Date must be after the From Date");
	}

	public void testValidateFuture() {
        Calendar end = new GregorianCalendar();
        end.set(2500,7,8);
        Calendar start = new GregorianCalendar();
        start.set(2500,7,4);

		String msg = dateValidator.validateDates(start.getTime(), end.getTime(), false);
		assertTrue(msg.contains("must not be in the future"));

		start = new GregorianCalendar();
        start.set(1980,7,4);
		msg = dateValidator.validateDates(start.getTime(), end.getTime(), false);
		assertTrue(msg.contains("must not be in the future"));
	}

	public void testValid() {
        Calendar end = new GregorianCalendar();
        end.set(2006,7,4);
        Calendar start = new GregorianCalendar();
        start.set(2006,7,1);

		String msg = dateValidator.validateDates(start.getTime(), end.getTime(), false);
		assertNull(msg);
	}

	protected void setUp() throws Exception {
		super.setUp();
		dateValidator = new DateValidator();
	}

	private DateValidator dateValidator;

}
