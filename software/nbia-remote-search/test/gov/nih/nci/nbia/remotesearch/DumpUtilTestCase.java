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
