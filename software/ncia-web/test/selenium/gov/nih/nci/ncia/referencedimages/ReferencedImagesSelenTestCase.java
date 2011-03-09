package gov.nih.nci.ncia.referencedimages;

import gov.nih.nci.ncia.AbstractSelenTestCaseImpl;

/**
 * Attempts to verify the Referenced Images Functionality from ISPY
 */
public class ReferencedImagesSelenTestCase extends AbstractSelenTestCaseImpl {
	/**
	 * Executes a specific I-Spy Image that was provided for us by Michael Harris and Subha. (the URL)
	 *
	 * Precondition: nciadevtest must have read access on ISPY collection
	 */
	public void testISpyReferencedImages() throws Exception {

        String image1Label ="Pre";

        navigateToISPYPortal("Pre",
                             "2",
                             "1.2.124.113532.192.9.54.60.20020702.141304.3659576",
                             "1.2.840.113619.2.5.1762805546.3105.1025559471.58",
                             "1.2.840.113619.2.5.1762805546.3105.1025559471.276",
                             "Post",
                             "2",
                             "1.2.124.113532.192.9.54.60.20021230.122345.403213",
                             "1.2.840.113619.2.5.1762805546.2376.1041867495.89",
                             "1.2.840.113619.2.5.1762805546.2376.1041867495.189");



		final String patientAttributesTable1 =
			//"id('MAINbody:imageForm:imageTable:patientAttributesTable')";
			"xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[1]/table[2]";

		final String patientAttributesTable2 =
			//"id('MAINbody:imageForm:imageTable:patientAttributesTable')";
			"xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[2]/table[2]";


		assertEquals(selenium.getText("xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[1]/table[1]/tbody/tr[1]/td"),
                     image1Label);

		assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[1]/td[1]"),
                     "Patient Id");
		assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[2]/td[1]"),
                     "Baseline Morphology");
		assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[3]/td[1]"),
                     "Longest Diameter_PCT_CHANGE_T1-T2");
		assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[4]/td[1]"),
                     "Clinical Response_T1-T2");

		assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[1]/td[2]"),
                     "2");
        assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[2]/td[2]"),
                     "3=Area enhancement with irregular margins - with nodularity");
        assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[3]/td[2]"),
                     "-10.34");
        assertEquals(selenium.getText(patientAttributesTable1+"/tbody/tr[4]/td[2]"),
                     "3=Stable Disease");




//
//		Longest Diameter_PCT_CHANGE_T1-T4
//		Clinical Response_T1-T4
//
//
//		3=Area enhancement with irregular margins - with nodularity
//		-44.83
//
//		3=Stable Disease
	}
}
