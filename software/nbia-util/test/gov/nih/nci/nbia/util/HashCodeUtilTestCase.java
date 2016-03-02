/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.HashCodeUtil;
import junit.framework.TestCase;

public class HashCodeUtilTestCase extends TestCase {

	public void testHashBoolean() {
		int result = HashCodeUtil.hash(42, true);
		assertTrue(result== (42*37+1));
		
		result = HashCodeUtil.hash(42, false);
		assertTrue(result== (42*37));		
	}
	
	
	public void testHashChar() {
		int result = HashCodeUtil.hash(42, 'z');
		assertTrue(result== (42*37+(int)'z'));
	}	
	
	
	public void testHashInt() {
		int result = HashCodeUtil.hash(42, 666);
		assertTrue(result== (42*37+666));
	}	
	
	public void testHashLong() {
		int result = HashCodeUtil.hash(42, (int) (100000666L ^ (100000666L >>> 32)) );
		assertTrue(result== (42*37+(int) (100000666L ^ (100000666L >>> 32))));
	}	
	
	public void testHashFloat() {
		int result = HashCodeUtil.hash(42, 2.0f);
		assertTrue(result== (42*37+Float.floatToIntBits(2.0f)));
	}	

	public void testHashDouble() {
		int result = HashCodeUtil.hash(42, 2.0);
		assertTrue(result== (42*37+ (int) (Double.doubleToLongBits(2.0) ^ (Double.doubleToLongBits(2.0) >>> 32)) ));
	}
	
	
	public void testHashObject() {
		int result = HashCodeUtil.hash(42, null);
		assertTrue(result== (42*37));
		
		Object fakeObj = new Object() {
			public int hashCode() {
				return 666;
			}
		};
		result = HashCodeUtil.hash(42, fakeObj);
		assertTrue(result== (42*37+666));		
		
		
		String[] arr = {"foo1", "foo2"};
		result = HashCodeUtil.hash(42, arr);
		int firstPass = 42*37+"foo1".hashCode();
		int secondPass = firstPass*37+"foo2".hashCode();
		assertTrue(result== secondPass);		
	}	
	
}
