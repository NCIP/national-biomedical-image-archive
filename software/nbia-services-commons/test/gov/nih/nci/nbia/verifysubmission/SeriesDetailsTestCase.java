/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import junit.framework.TestCase;

public class SeriesDetailsTestCase extends TestCase {

	public void testSeriesDetails() {
		SeriesDetails seriesDetails = new SeriesDetails("someString", 3);
		assertTrue(seriesDetails.getSeriesInstanceUid().equals("someString"));
		assertTrue(seriesDetails.getSubmissionCount()==3);
	}
}
