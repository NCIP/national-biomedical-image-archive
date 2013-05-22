/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

public class DataFieldTypeMapTestCase extends TestCase {

	public void testGetDataFieldType() throws Exception {
		String className = DataFieldTypeMap.getDataFieldType("garbage");
		assertNull(className);

		className = DataFieldTypeMap.getDataFieldType("KVP");
		assertTrue(className.equals("java.lang.Double"));

		className = DataFieldTypeMap.getDataFieldType("gantryDetectorTilt");
		assertTrue(className.equals("java.lang.Double"));
	}

	protected void setUp() throws Exception {
		super.setUp();
		URL url = TableRelationships.class.getClassLoader().getResource("relationship.xml");
		File file = new File(url.toURI());
		System.setProperty("jboss.server.data.dir", file.getParent());
	}

}
