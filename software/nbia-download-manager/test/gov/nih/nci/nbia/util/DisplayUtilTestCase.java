/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import junit.framework.TestCase;
import gov.nih.nci.nbia.util.DisplayUtil;

public class DisplayUtilTestCase extends TestCase {

	public void testComputeSizeString() {

		long sizeInBytes = 2;
		String s1 = DisplayUtil.computeSizeString(sizeInBytes);
		//System.out.println("s1: "+s1);
		assertEquals(s1, "2 Bytes");

		sizeInBytes = 1000000;
		s1 = DisplayUtil.computeSizeString(sizeInBytes);
		//System.out.println("s1: "+s1);
		assertEquals(s1, "976.6 KB");

		sizeInBytes = 1000000000;
		s1 = DisplayUtil.computeSizeString(sizeInBytes);
		//System.out.println("s1: "+s1);
		assertEquals(s1, "953.7 MB");

		sizeInBytes = 2100000000;
		s1 = DisplayUtil.computeSizeString(sizeInBytes);
		//System.out.println("s1: "+s1);
		assertEquals(s1, "2 GB");

	}
}
