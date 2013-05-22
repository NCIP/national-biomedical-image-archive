/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class CustomSeriesListDTOTestCase extends TestCase {
	
	public void testAccessors() {
	    CustomSeriesListAttributeDTO dto = new CustomSeriesListAttributeDTO(2233, "seriesId");
	  		
		CustomSeriesListDTO customSeriesListDTO = new CustomSeriesListDTO();
		List<CustomSeriesListAttributeDTO> seriesInstanceUidsList = new ArrayList<CustomSeriesListAttributeDTO> ();
		seriesInstanceUidsList.add(dto);
		List<String> seriesInstanceUIDs = new ArrayList<String>();
		seriesInstanceUIDs.add("seriesId");
		
		customSeriesListDTO.setSeriesInstanceUidsList(seriesInstanceUidsList);
		customSeriesListDTO.setName("Name");
		customSeriesListDTO.setComment("Comment");
		customSeriesListDTO.setHyperlink("https://imaging.nci.nih.gov/ncia");
		customSeriesListDTO.setSeriesInstanceUIDs(seriesInstanceUIDs);
		
		assertTrue(dto.getId() == 2233);		
		assertTrue(dto.getSeriesInstanceUid().equals("seriesId"));	
		
		assertTrue(customSeriesListDTO.getSeriesInstanceUidsList().size() == 1);
		assertTrue(customSeriesListDTO.getName().equals("Name"));
		assertTrue(customSeriesListDTO.getComment().equals("Comment"));
		assertTrue(customSeriesListDTO.getSeriesInstanceUIDs().size() == 1);
		assertTrue(customSeriesListDTO.getHyperlink().equals("https://imaging.nci.nih.gov/ncia"));
	}

}
