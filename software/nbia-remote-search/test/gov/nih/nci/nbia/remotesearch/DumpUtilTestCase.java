/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.remotesearch;

import org.junit.Test;

public class DumpUtilTestCase {

	@Test
	public void testDumpArray() {
		DumpUtil.dumpArray("foo", null);
		DumpUtil.dumpArray("foo", new Object[]{null, null});

	}

	@Test
	public void testDebug() {
		DumpUtil.debug(null);
	}
}
