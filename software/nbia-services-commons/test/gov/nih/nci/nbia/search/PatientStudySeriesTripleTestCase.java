/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

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
