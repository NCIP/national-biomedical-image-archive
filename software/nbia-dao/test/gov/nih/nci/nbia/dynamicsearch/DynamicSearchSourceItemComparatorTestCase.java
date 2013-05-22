/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch;

import gov.nih.nci.nbia.xmlobject.SourceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;

public class DynamicSearchSourceItemComparatorTestCase extends TestCase {

	public void testEqual() {
		SourceItem s1 = new SourceItem();
		s1.setItemLabel("l1");
		
		SourceItem s2 = new SourceItem();
		s2.setItemLabel("l1");
		
		int result = comparator.compare(s1, s2);
		assertEquals(result, 0);
	}
	
	public void testNotEqual() {
		SourceItem s1 = new SourceItem();
		s1.setItemLabel("l1");
		
		SourceItem s2 = new SourceItem();
		s2.setItemLabel("l2");
		
		int result = comparator.compare(s1, s2);
		super.assertNotSame(result, 0);
	}	
	
	
	public void testNull() {
		SourceItem s1 = new SourceItem();
		s1.setItemLabel("l1");
		
		int result = comparator.compare(null, null);
		assertEquals(result, 0);
		
		result = comparator.compare(null, s1);
		super.assertNotSame(result, 0);
		
		result = comparator.compare(s1, null);
		super.assertNotSame(result, 0);		
		
		List<SourceItem> collection = new ArrayList<SourceItem>();
		collection.add(null);
		collection.add(null);
		collection.add(s1);
		Collections.sort(collection, comparator);
		
		assertEquals(collection.get(0), s1);
		assertNull(collection.get(1));
		assertNull(collection.get(2));
		
	}	

	protected void setUp() {
		comparator = new DynamicSearchSourceItemComparator();
	}
	
	private Comparator<SourceItem> comparator;
}
