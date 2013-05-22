/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.UidDisplayUtil;
import junit.framework.TestCase;

public class UidDisplayUtilTestCase extends TestCase {
	public void testNullInput() {
		String trunc = UidDisplayUtil.getDisplayUid(null);
		assertNull(trunc);
	}
		
	public void testGetDisplayUidBeforeSettingSystemProperty() {
	
		//default to 100
		String trunc = UidDisplayUtil.getDisplayUid("01234567890123456789");
		assertTrue(trunc.equals("01234567890123456789"));
		
		System.setProperty("gov.nih.nci.ncia.ui.uid.display.length", "");
		trunc = UidDisplayUtil.getDisplayUid("01234567890123456789");
		assertTrue(trunc.equals("01234567890123456789"));
	}		

	public void testGetDisplayUid() {
		//110
		String trunc = UidDisplayUtil.getDisplayUid("01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		
		//100/2 = first 50 and last 50
		assertTrue(trunc.equals("01234567890123456789012345678901234567890123456789...01234567890123456789012345678901234567890123456789"));
	}
	
	public void testGetDisplayUidAfterSettingSystemPropertyErroneously() {
		System.setProperty("gov.nih.nci.ncia.ui.uid.display.length", "not_a_number");
		
		//default to 100
		String trunc = UidDisplayUtil.getDisplayUid("01234567890123456789");
		assertTrue(trunc.equals("01234567890123456789"));
		
	}	
	public void testGetDisplayUidAfterSettingSystemProperty() {
		System.setProperty("gov.nih.nci.ncia.ui.uid.display.length", "10");		
		String trunc = UidDisplayUtil.getDisplayUid("01234567890123456789");
		assertTrue(trunc.equals("01234...56789"));
	}
	
	

}
