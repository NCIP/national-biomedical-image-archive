/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.searchresult;

import junit.framework.TestCase;

public class ModelTestCase extends TestCase {

	public void testModel() {
		String[] versions = { "v1", "v2", "v3"};
		Model model = new Model();
		model.setName("foo1");
		model.setVersions(versions);
		assertTrue(model.getName().equals("foo1"));
		assertTrue(model.getVersions().length==3);
		
		model.setVersions(1, "moo");
		assertTrue(model.getVersions(1).equals("moo"));
	}

}
