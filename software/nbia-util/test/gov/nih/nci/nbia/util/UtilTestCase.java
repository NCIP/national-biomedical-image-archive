/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class UtilTestCase extends TestCase {

	public void testLoadSystemPropertiesFromPropertiesResource() {
		Util.loadSystemPropertiesFromPropertiesResource("someproperties.properties");
		
		assertEquals(System.getProperty("foo"),"moo");
		assertEquals(System.getProperty("cow"),"yowzer");
		assertEquals(System.getProperty("mastodon"),"rocks");		
		
		try {
			Util.loadSystemPropertiesFromPropertiesResource("not_to_be_found");
			fail("should fail");
		}
		catch(RuntimeException e) {			
		}
	}
	
	public void testHasAtLeastOneNonBlankArgument() {
		assertFalse(Util.hasAtLeastOneNonBlankArgument((String[])null));

		assertFalse(Util.hasAtLeastOneNonBlankArgument());
		
		assertTrue(Util.hasAtLeastOneNonBlankArgument("foo"));
		
		assertFalse(Util.hasAtLeastOneNonBlankArgument("", "", ""));
		
		assertTrue(Util.hasAtLeastOneNonBlankArgument("", "foo"));
		
	}
	public void testbreakListIntoChunks() {
		List<Integer> list = new ArrayList<Integer>(100);
		for(int i=0;i<100;i++) {
			list.add(i);
		}
		
		List<List<Integer>> chunks = Util.breakListIntoChunks(list, 5);
		System.out.println("chunks:"+chunks.get(0).get(4));
		assertTrue(chunks.size()==20);		
		assertTrue(chunks.get(0).get(4)==4);
		assertTrue(chunks.get(19).get(4)==99);
		
		Util.dumpCollection(list);
	}
	
	
	public void testNullSafeString() {
		Object nullObject = null;
		assertTrue(Util.nullSafeString(nullObject)==null);
		
		Object o = new Object() {
			public String toString() {
				return "foo";
			}
		};
		
		assertTrue(Util.nullSafeString(o).equals("foo"));
	}
	
	public void testGetCommaSeparatedList() {

		Collection<String> collection = new ArrayList<String>();
		String s = Util.getCommaSeparatedList(collection);
		assertEquals(s, "");
		
		collection.add("foo");
		s = Util.getCommaSeparatedList(collection);
		assertEquals(s, "foo");

		collection = new ArrayList<String>();
		collection.add("foo");
		collection.add("cow");
		collection.add("moo");
		s = Util.getCommaSeparatedList(collection);
		assertEquals(s, "foo, cow, moo");
	}

	public void testIsEmptyCollectionTrue() {
		Collection<Object> nullCollection = null;
		assertTrue(Util.isEmptyCollection(nullCollection)==true);

		Collection<Object> emptyCollection = new ArrayList<Object>();
		assertTrue(Util.isEmptyCollection(emptyCollection)==true);
	}

	public void testIsEmptyCollectionFalse() {
		Collection<Object> notEmptyCollection = new ArrayList<Object>();
		notEmptyCollection.add(new Object());
		assertTrue(Util.isEmptyCollection(notEmptyCollection)==false);
	}

	public void testHasAtLeastOneNonNullArgumentTrue() {
		assertTrue(Util.hasAtLeastOneNonNullArgument(new Object())==true);

		assertTrue(Util.hasAtLeastOneNonNullArgument(null, null, new Object())==true);

		assertTrue(Util.hasAtLeastOneNonNullArgument(null, new Object(), new Object())==true);
	}

	public void testHasAtLeastOneNonNullArgumentFalse() {
		assertTrue(Util.hasAtLeastOneNonNullArgument()==false);

		assertTrue(Util.hasAtLeastOneNonNullArgument((Object[])null)==false);

		assertTrue(Util.hasAtLeastOneNonNullArgument(null, null, null)==false);
	}

	public void testCalculateOffsetValue() throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date date1 = sdf.parse("02-01-2009");
		Date date2 = sdf.parse("03-24-2009");
		System.out.println("date 1: " + date1 + " date2: " + date2);
		assertTrue(Util.calculateOffsetValue(date1, date2).equalsIgnoreCase("2 month(s)"));

		Date date11 = sdf.parse("12-20-2008");
		Date date22 = sdf.parse("01-05-2009");
		System.out.println("date 11: " + date11 + " date22: " + date22);
		assertTrue(Util.calculateOffsetValue(date11, date22).equalsIgnoreCase("1 month(s)"));


		date1 = sdf.parse("01-01-2009");
		date2 = sdf.parse("01-05-2009");
		assertTrue(Util.calculateOffsetValue(date1, date2).equalsIgnoreCase("4 day(s)"));
		
		assertNull(Util.calculateOffsetValue(date2, date1));		
	}

	public void testCurrentDateTimeString() {
		assertNotNull(Util.currrentDateTimeString());		
	}
}
