/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.NameValueObj;
import junit.framework.TestCase;

public class NameValueObjTestCase extends TestCase {

	public void testGetName() {
		NameValueObj nameValueObj = new NameValueObj();
		
		nameValueObj.setName("foo1");
		nameValueObj.setValue("foo2");
		
		assertEquals(nameValueObj.getName(), "foo1");
		assertEquals(nameValueObj.getValue(), "foo2");
		
	}

}
