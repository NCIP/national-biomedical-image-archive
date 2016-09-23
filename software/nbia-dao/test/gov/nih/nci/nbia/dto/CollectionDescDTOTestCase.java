/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

public class CollectionDescDTOTestCase extends TestCase {
	
	public void testAccessors() {
		CollectionDescDTO collectionDescDTO = new CollectionDescDTO();
		collectionDescDTO.setCollectionName("TEST");
		collectionDescDTO.setUserName("lethai");
		collectionDescDTO.setDescription("<p>TEST collection description.</p>");
		collectionDescDTO.setId(2233);
		assertTrue(collectionDescDTO.getId() == 2233);		
		assertTrue(collectionDescDTO.getCollectionName().equals("TEST"));		
		assertTrue(collectionDescDTO.getDescription().equals("<p>TEST collection description.</p>"));
		assertTrue(collectionDescDTO.getUserName().equals("lethai"));		
	}
}