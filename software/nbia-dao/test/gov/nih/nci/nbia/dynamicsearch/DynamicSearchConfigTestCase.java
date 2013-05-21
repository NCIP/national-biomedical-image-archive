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
