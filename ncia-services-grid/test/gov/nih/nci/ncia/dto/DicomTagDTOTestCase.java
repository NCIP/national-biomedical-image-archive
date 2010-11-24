package gov.nih.nci.ncia.dto;

import junit.framework.TestCase;

public class DicomTagDTOTestCase extends TestCase {

	public void testDicomTagDTO() {
		DicomTagDTO dicomTagDTO = new DicomTagDTO();
		dicomTagDTO.setData("data1");
		dicomTagDTO.setElement("element1");
		dicomTagDTO.setName("name1");
		
		assertEquals(dicomTagDTO.getData(),"data1");
		assertEquals(dicomTagDTO.getElement(),"element1");
		assertEquals(dicomTagDTO.getName(),"name1");	
		
		
		dicomTagDTO = new DicomTagDTO("elt2", "name2", "data2");
		assertEquals(dicomTagDTO.getData(),"data2");
		assertEquals(dicomTagDTO.getElement(),"elt2");
		assertEquals(dicomTagDTO.getName(),"name2");		
	}
}
