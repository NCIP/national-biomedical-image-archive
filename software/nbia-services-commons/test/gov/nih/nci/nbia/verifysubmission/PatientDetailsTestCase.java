/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.verifysubmission;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class PatientDetailsTestCase extends TestCase {

	public void testGetPatientId() {	
		SeriesDetails seriesDetail1 = new SeriesDetails("s1", 5);		
		SeriesDetails seriesDetail2 = new SeriesDetails("s2", 3);
		List<SeriesDetails> seriesDetails = new ArrayList<SeriesDetails>();
		seriesDetails.add(seriesDetail1);
		seriesDetails.add(seriesDetail2);
				
		StudyDetails studyDetails1 = new StudyDetails("studyUid1", seriesDetails);
		//ok to reuse for test
		StudyDetails studyDetails2 = new StudyDetails("studyUid2", seriesDetails);
		List<StudyDetails> studyDetails = new ArrayList<StudyDetails>();
		studyDetails.add(studyDetails1);
		studyDetails.add(studyDetails2);

		PatientDetails patientDetails = new PatientDetails("p1", studyDetails);
		assertTrue(patientDetails.getPatientId().equals("p1"));
		assertTrue(patientDetails.getStudyCount()==2);
		assertTrue(patientDetails.getSeriesCount()==(2*2));
		assertTrue(patientDetails.getSubmissionCount()==((5+3)*2));
		assertTrue(patientDetails.getStudyDetails().size()==2);
	}

	public void testIllegitStudies() {
		List<StudyDetails> emptyStudyDetails = new ArrayList<StudyDetails>();
		
		try {
			new PatientDetails("patientId", null);
			fail();
		}
		catch(Exception ex) {
			//ok
		}
		try {
			new PatientDetails("patientId", emptyStudyDetails);
			fail();
		}
		catch(Exception ex) {
			//ok.
		}
	}		
}
