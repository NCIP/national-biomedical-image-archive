/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import gov.nih.nci.nbia.dto.DicomTagDTO;
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
