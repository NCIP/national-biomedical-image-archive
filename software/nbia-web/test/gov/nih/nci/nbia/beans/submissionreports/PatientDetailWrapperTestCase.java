/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import junit.framework.TestCase;

public class PatientDetailWrapperTestCase extends TestCase {

	public void testPatientDetailWrapper() {
		PatientDetailWrapper patientDetailWrapper  = new PatientDetailWrapper("patient", "study", "series", "img");
		assertEquals(patientDetailWrapper.getPatient(), "patient");
		assertEquals(patientDetailWrapper.getStudy(), "study");
		assertEquals(patientDetailWrapper.getSeries(), "series");
		assertEquals(patientDetailWrapper.getImg(), "img");
	}

}
