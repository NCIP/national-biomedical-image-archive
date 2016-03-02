/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.StringEncrypter;
import junit.framework.TestCase;

public class StringEncrypterTestCase extends TestCase {

	public void testEncryptHappyPathDESede() throws Exception {
		String origText = "this is plain text";
		
		StringEncrypter stringEncrypter = new StringEncrypter();
		String encryptedText = stringEncrypter.encryptString(origText);
		
		//not really a great assertion, but it shoudl be true :)
		assertFalse(encryptedText.equals(origText));
		
	
		stringEncrypter = new StringEncrypter();
		String decryptedText = stringEncrypter.decryptString(encryptedText);
		
		assertTrue(decryptedText.equals(origText));
	}
	
	public void testEncryptHappyPathDES() throws Exception {
		String origText = "this is plain text";
		
		StringEncrypter stringEncrypter = new StringEncrypter();
		String encryptedText = stringEncrypter.encryptString(origText);
		
		//not really a great assertion, but it shoudl be true :)
		assertFalse(encryptedText.equals(origText));
		
	
		stringEncrypter = new StringEncrypter();
		String decryptedText = stringEncrypter.decryptString(encryptedText);
		
		assertTrue(decryptedText.equals(origText));
	}	

	
	public void testEncryptErrors() throws Exception {		
		/*try {
			new StringEncrypter("DES", null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}
		try {
			new StringEncrypter("DES", "012345789");
			fail("shorty key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}	
		try {
			new StringEncrypter("fake_encryption_scheme", "012345789012345789012345789012345789");
			fail("non DES shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}*/	
		
		try {
			StringEncrypter stringEncrypter = new StringEncrypter();
			stringEncrypter.encryptString(null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null string
		}
		
		try {
			StringEncrypter stringEncrypter = new StringEncrypter();
			stringEncrypter.decryptString(null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null string
		}		
	}	
}
