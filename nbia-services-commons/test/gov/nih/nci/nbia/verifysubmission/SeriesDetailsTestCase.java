package gov.nih.nci.nbia.verifysubmission;

import junit.framework.TestCase;

public class SeriesDetailsTestCase extends TestCase {

	public void testSeriesDetails() {
		SeriesDetails seriesDetails = new SeriesDetails("someString", 3);
		assertTrue(seriesDetails.getSeriesInstanceUid().equals("someString"));
		assertTrue(seriesDetails.getSubmissionCount()==3);
	}
}
