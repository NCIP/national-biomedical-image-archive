/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.ncia.search.NBIANode;

import java.util.List;
import java.util.concurrent.ExecutorService;
import junit.framework.TestCase;

public class PatientSearcherServiceFactoryTestCase extends TestCase {

	public void testGetPatientSearcherServiceFailure() {
		//dont set sys property "thumbnailResolver.className"

		try {
			PatientSearcherServiceFactory.getPatientSearcherService();
			fail("shouldnt get here");
		}
		catch(Exception ex) {
			//should get here
		}

	}

	public void testGetPatientSearcherService() {
		System.setProperty("patientSearcherService.className",
				           "gov.nih.nci.nbia.search.PatientSearcherServiceFactoryTestCase$FakePatientSearcherService");

		PatientSearcherService patientSearcherService = PatientSearcherServiceFactory.getPatientSearcherService();
		PatientSearcherService patientSearcherService2 = PatientSearcherServiceFactory.getPatientSearcherService();

		assertEquals(patientSearcherService, patientSearcherService2);
		assertTrue(patientSearcherService instanceof FakePatientSearcherService);
	}

	public static class FakePatientSearcherService implements PatientSearcherService {
		public PatientSearchCompletionService searchForPatients(List<NBIANode> nodesToSearch,
                                                                DICOMQuery dicomQuery) {
			return null;
		}

		public void setExecutorService(ExecutorService executorService) {

		}
	}

}
