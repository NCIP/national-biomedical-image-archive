package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;
import java.util.Date;

public class DayCountDTOTestCase extends TestCase {

	public void testConstructorAndAccessors() {
		Date date = new Date();
		DayCountDTO dayCountDTO = new DayCountDTO(date, 4);
		
		assertEquals(dayCountDTO.getDay(), date);
		assertEquals(dayCountDTO.getSubmissionCount(), 4);
	}
}
