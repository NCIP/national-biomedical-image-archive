/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class StringUtilTestCase extends TestCase {
	public void testEncodeListEntriesWithSingleQuotes() {
		List<String> emptyList = new ArrayList<String>();
		String result = StringUtil.encodeListEntriesWithSingleQuotes(emptyList);
		assertEquals(result, "");
		
		List<String> singletonList = java.util.Collections.singletonList("foo");
		result = StringUtil.encodeListEntriesWithSingleQuotes(singletonList);
		assertEquals(result, "'foo'");
		
		List<String> twoEntryList = new ArrayList<String>();
		twoEntryList.add("foo1");
		twoEntryList.add("foo2");
		result = StringUtil.encodeListEntriesWithSingleQuotes(twoEntryList);
		assertEquals(result, "'foo1','foo2'");		
	}
	public void testDisplayAsSixDigitString() {
		String actual = StringUtil.displayAsSixDigitString(0);
		
		assertEquals("000000", actual);
		
		actual = StringUtil.displayAsSixDigitString(10);
		
		assertEquals("000010", actual);
		
		actual = StringUtil.displayAsSixDigitString(100);
		
		assertEquals("000100", actual);				
		
	}
	public void testLastToken() {
		
		String lastToken = StringUtil.lastToken("foo,cow,moo", ",");
		assertEquals(lastToken, "moo");

		assertNull(StringUtil.lastToken("foo,cow,moo", "|"));

	}
	public void testNotEmpty() {
		String testStr = "x";
		assertTrue(!StringUtil.isEmpty(testStr));
		
		testStr = "notherstring";
		assertTrue(!StringUtil.isEmpty(testStr));
		
		testStr = " ";
		assertTrue(!StringUtil.isEmpty(testStr));		
	}
	
	public void testEmpty() {
		String testStr = "";
		assertTrue(StringUtil.isEmpty(testStr));
		
		testStr = null;
		assertTrue(StringUtil.isEmpty(testStr));
	}	
	
	public void testNotEmptyTrim() {
		String testStr = "x";
		assertTrue(!StringUtil.isEmptyTrim(testStr));
		
		testStr = "notherstring";
		assertTrue(!StringUtil.isEmptyTrim(testStr));		
	}
	
	public void testEmptyTrim() {
		String testStr = "";
		assertTrue(StringUtil.isEmptyTrim(testStr));
		
		testStr = null;
		assertTrue(StringUtil.isEmptyTrim(testStr));
		
		testStr = " ";
		assertTrue(StringUtil.isEmptyTrim(testStr));	
		
		testStr = "    ";
		assertTrue(StringUtil.isEmptyTrim(testStr));		
	}	
	
    public void testRemoveCharacters(){
    	
    	boolean doesContainspChars = false;
    	String test = StringUtil.removeIllegitCharacters("1234%20STYLE=xss:e/**/xpression(try{a=firstTime}catch(e){firstTime=1;alert(42646)})");
    	doesContainspChars = StringUtil.doesContainIllegitCharacters(test);
    	assertFalse("Does not contain sp chars", doesContainspChars);    	
    	
    	assertTrue(StringUtil.doesContainIllegitCharacters("foo/cow"));
    }	

    public void testToHexString() {
    	byte[] bytes = new byte[5];
    	bytes[0] = 0x01;
    	bytes[1] = 0x0a;
    	bytes[2] = 0x16;
    	bytes[3] = 0x60;
    	bytes[4] = 0x04;
    	String output = StringUtil.toHexString(bytes);
    	assertEquals(output, "01 0a 16 60 04 ");
    }
    
    public void testRemoveHTML(){
    	String s = "<p>This is a paragraph of text.</p><b>A bold text</b> and another text.";
    	String output = StringUtil.removeHTML(s);
    	System.out.println("output: " + output);
    	assertEquals(output, "This is a paragraph of text.A bold text and another text.");
    }
}
