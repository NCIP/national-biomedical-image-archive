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
