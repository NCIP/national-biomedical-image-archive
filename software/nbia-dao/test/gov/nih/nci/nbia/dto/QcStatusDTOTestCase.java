package gov.nih.nci.nbia.dto;

import java.util.Date;

import junit.framework.TestCase;

public class QcStatusDTOTestCase extends TestCase {

	public void testAccessor() {
		QcStatusDTO qcStatusDTO = new QcStatusDTO(new Date(123456), "1.1","1","0"); 
		
		assertTrue(qcStatusDTO.getTimeStamp().getTime()==123456);
		assertTrue(qcStatusDTO.getSeries().equals("1.1"));
		assertTrue(qcStatusDTO.getNewStatus().equals("1"));
		assertTrue(qcStatusDTO.getOldStatus().equals("0"));
	}

}
