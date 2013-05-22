/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import junit.framework.TestCase;

public class AnnotationCountReportTestCase extends TestCase {

	public void testAccessors() {
		AnnotationCountReport annotationCountReport =
			new AnnotationCountReport(0,1,2,3);
		
		assertTrue(annotationCountReport.getAffectedPatientCount()==0);
		assertTrue(annotationCountReport.getAffectedStudyCount()==1);
		assertTrue(annotationCountReport.getAffectedSeriesCount()==2);
		assertTrue(annotationCountReport.getAnnotationSubmissionCount()==3);
	}

}
