/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import java.io.File;

import junit.framework.TestCase;

public class DynamicSearchConfigTestCase extends TestCase {
	public void testGetDataSourceItemConfigFile() {
		System.setProperty("jboss.server.data.dir", "/");
		
		File dataSourceItemFile = DynamicSearchConfig.getDataSourceItemConfigFile();
		assertNotNull(dataSourceItemFile);
		assertEquals(dataSourceItemFile.getName(), "DataSourceItem.xml");
	}
	
	public  void testGetRelationshipConfigFile() {
		System.setProperty("jboss.server.data.dir", "/");
		
		File relationshipConfigFile = DynamicSearchConfig.getRelationshipConfigFile();
		assertNotNull(relationshipConfigFile);
		assertEquals(relationshipConfigFile.getName(), "relationship.xml");		
	}
}
