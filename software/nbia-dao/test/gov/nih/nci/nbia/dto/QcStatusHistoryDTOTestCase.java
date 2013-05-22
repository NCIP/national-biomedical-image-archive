/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.util.Date;

import junit.framework.TestCase;

public class QcStatusHistoryDTOTestCase extends TestCase {

	public void testAccessor() { 
		QcStatusHistoryDTO qcStatusHistoryDTO = new QcStatusHistoryDTO(new Date(123456), "1.1","1","0","Test","uid"); 
		
		assertTrue(qcStatusHistoryDTO.getTimeStamp().getTime()==123456);
		assertTrue(qcStatusHistoryDTO.getSeries().equals("1.1"));
		assertTrue(qcStatusHistoryDTO.getNewStatus().equals("1"));
		assertTrue(qcStatusHistoryDTO.getOldStatus().equals("0"));
		assertTrue(qcStatusHistoryDTO.getComment().equals("Test"));
		assertTrue(qcStatusHistoryDTO.getUserId().equals("uid"));
	}

}
