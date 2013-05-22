/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import junit.framework.TestCase;

public class ImageSubmissionCountReportTestCase extends TestCase {

	public void testAccessors() {
		ImageSubmissionCountReport imageSubmissionCountReport =
			new ImageSubmissionCountReport(0,1,2,3,4,5,6,7);
		
		assertTrue(imageSubmissionCountReport.getAffectedPatientCount()==0);
		assertTrue(imageSubmissionCountReport.getAffectedStudyCount()==1);
		assertTrue(imageSubmissionCountReport.getAffectedSeriesCount()==2);
		assertTrue(imageSubmissionCountReport.getSubmittedImageCount()==3);
		assertTrue(imageSubmissionCountReport.getCorrectedPatientCount()==4);
		assertTrue(imageSubmissionCountReport.getCorrectedStudyCount()==5);
		assertTrue(imageSubmissionCountReport.getCorrectedSeriesCount()==6);
		assertTrue(imageSubmissionCountReport.getCorrectedImageCount()==7);		
	}

}
