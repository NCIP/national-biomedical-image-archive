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
