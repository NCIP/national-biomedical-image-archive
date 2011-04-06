package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

public class CustomSeriesListAttributeDTOTestCase extends TestCase {

	
	public void testAccessors() {
	    CustomSeriesListAttributeDTO dto = new CustomSeriesListAttributeDTO(2233, "seriesId");
	    assertTrue(dto.getId() == 2233);		
		assertTrue(dto.getSeriesInstanceUid().equals("seriesId"));	
	}

}
