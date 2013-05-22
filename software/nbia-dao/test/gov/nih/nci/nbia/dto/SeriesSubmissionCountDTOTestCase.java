/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

public class SeriesSubmissionCountDTOTestCase extends TestCase {

	public void testAccessors() {
		SeriesSubmissionCountDTO seriesSubmissionCountDTO = 
			new SeriesSubmissionCountDTO("patientId",
					                     "studyId",
					                     "seriesId",
					                     666);
		assertTrue(seriesSubmissionCountDTO.getPatientId().equals("patientId"));
		assertTrue(seriesSubmissionCountDTO.getStudyInstanceUid().equals("studyId"));
		assertTrue(seriesSubmissionCountDTO.getSeriesInstanceUid().equals("seriesId"));
		assertTrue(seriesSubmissionCountDTO.getSubmissionCount()==666);		
	}

}
