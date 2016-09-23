/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

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
