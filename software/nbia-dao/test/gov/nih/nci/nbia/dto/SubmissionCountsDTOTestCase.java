/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dto;

import junit.framework.TestCase;

public class SubmissionCountsDTOTestCase extends TestCase {

	public void testAccessor() {
		SubmissionCountsDTO submissionCountsDTO = new SubmissionCountsDTO(0,1,2,3);
		assertTrue(submissionCountsDTO.getPatientCount()==0);
		assertTrue(submissionCountsDTO.getStudyCount()==1);
		assertTrue(submissionCountsDTO.getSeriesCount()==2);
		assertTrue(submissionCountsDTO.getSubmissionCount()==3);
	}

}
