/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.submissionreports;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class PatientDetailGroupWrapperTestCase extends TestCase {

	public void testPatientDetailGroupWrapper() {
		List<PatientDetailGroupWrapper> topLevelList = new ArrayList<PatientDetailGroupWrapper>();

		PatientDetailGroupWrapper patientDetailWrapper0  =
			new PatientDetailGroupWrapper("patient0",
					                      "study0",
					                      "series0",
					                      "img0",
					                      topLevelList);

		PatientDetailGroupWrapper patientDetailWrapper1  =
			new PatientDetailGroupWrapper("patient1",
					                      "study1",
					                      "series1",
					                      "img1",
					                      topLevelList);

		PatientDetailGroupWrapper patientDetailWrapper_mid  =
			new PatientDetailGroupWrapper("patientmid",
					                      "studymid",
					                      "seriesmid",
					                      "imgmid",
					                      topLevelList);
		patientDetailWrapper_mid.getChildren().add(patientDetailWrapper0);
		patientDetailWrapper_mid.getChildren().add(patientDetailWrapper1);


		PatientDetailGroupWrapper patientDetailGroupWrapper
			= new PatientDetailGroupWrapper("patienttop",
                                            "studytop",
                                            "seriestop",
                                            "imgtop",
                                            topLevelList);
		patientDetailGroupWrapper.getChildren().add(patientDetailWrapper_mid);

		assertEquals(patientDetailGroupWrapper.getPatient(), "patienttop");
		assertEquals(patientDetailGroupWrapper.getStudy(), "studytop");
		assertEquals(patientDetailGroupWrapper.getSeries(), "seriestop");
		assertEquals(patientDetailGroupWrapper.getImg(), "imgtop");

		assertEquals(patientDetailGroupWrapper.getExpandContractImage(),
				     "/xmlhttp/css/rime/css-images/tree_nav_top_open_no_siblings.gif");

		patientDetailGroupWrapper.toggleSubGroupAction(null);
		assertEquals(patientDetailGroupWrapper.getExpandContractImage(),
             	     "/xmlhttp/css/rime/css-images/tree_nav_top_close_no_siblings.gif");

		patientDetailGroupWrapper.toggleSubGroupAction(null);
		assertEquals(patientDetailGroupWrapper.getExpandContractImage(),
	                 "/xmlhttp/css/rime/css-images/tree_nav_top_open_no_siblings.gif");
	}

}
