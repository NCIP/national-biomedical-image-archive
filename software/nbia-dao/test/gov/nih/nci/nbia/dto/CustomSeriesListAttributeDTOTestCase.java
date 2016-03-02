/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

public class CustomSeriesListAttributeDTOTestCase extends TestCase {

	
	public void testAccessors() {
	    CustomSeriesListAttributeDTO dto = new CustomSeriesListAttributeDTO(2233, "seriesId");
	    assertTrue(dto.getId() == 2233);		
		assertTrue(dto.getSeriesInstanceUid().equals("seriesId"));	
	}

}
