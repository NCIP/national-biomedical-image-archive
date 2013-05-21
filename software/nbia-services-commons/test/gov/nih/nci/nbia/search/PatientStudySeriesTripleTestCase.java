package gov.nih.nci.nbia.search;

import junit.framework.TestCase;

public class PatientStudySeriesTripleTestCase extends TestCase {

	public void testGetPatientPkId() {
		PatientStudySeriesTriple patientStudySeriesTriple = new PatientStudySeriesTriple();
		patientStudySeriesTriple.setPatientPkId(1);
		patientStudySeriesTriple.setStudyPkId(2);
		patientStudySeriesTriple.setSeriesPkId(3);
		
		assertTrue(patientStudySeriesTriple.getPatientPkId()==1);
		assertTrue(patientStudySeriesTriple.getStudyPkId()==2);
		assertTrue(patientStudySeriesTriple.getSeriesPkId()==3);		
	}

}
