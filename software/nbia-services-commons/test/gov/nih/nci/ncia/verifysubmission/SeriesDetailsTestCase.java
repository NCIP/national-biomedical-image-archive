package gov.nih.nci.ncia.verifysubmission;

import gov.nih.nci.nbia.verifysubmission.SeriesDetails;
import junit.framework.TestCase;

public class SeriesDetailsTestCase extends TestCase {

	public void testSeriesDetails() {
		SeriesDetails seriesDetails = new SeriesDetails("someString", 3);
		assertTrue(seriesDetails.getSeriesInstanceUid().equals("someString"));
		assertTrue(seriesDetails.getSubmissionCount()==3);
	}
}
