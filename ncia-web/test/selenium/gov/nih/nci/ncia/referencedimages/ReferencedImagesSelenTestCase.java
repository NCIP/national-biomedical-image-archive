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

		selenium.open("/ncia/referencedImages.jsf?source=ISPY&image1Label="+image1Label+
		              "&image1TrialId=ISPY&image1PatientId=2&image1StudyInstanceUid=1.2.124.113532.192.9.54.60.20020702.141304.3659576&image1SeriesInstanceUid=1.2.840.113619.2.5.1762805546.3105.1025559471.58&image1ImageSopInstanceUid=1.2.840.113619.2.5.1762805546.3105.1025559471.276&image1dataName=Patient%20Id&image1dataValue=2&image1dataName=Baseline%20Morphology&image1dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity&image1dataName=Longest%20Diameter_PCT_CHANGE_T1-T2&image1dataValue=-10.34&image1dataName=Clinical%20Response_T1-T2&image1dataValue=3=Stable%20Disease&image2StudyInstanceUid=1.2.124.113532.192.9.54.60.20021230.122345.403213&image2SeriesInstanceUid=1.2.840.113619.2.5.1762805546.2376.1041867495.89&image2ImageSopInstanceUid=1.2.840.113619.2.5.1762805546.2376.1041867495.189&image2Label=Post&image2TrialId=ISPY&image2PatientId=2&image2dataName=Patient%20Id&image2dataValue=2&image2dataName=Baseline%20Morphology&image2dataValue=3=Area%20enhancement%20with%20irregular%20margins%20-%20with%20nodularity&image2dataName=Longest%20Diameter_PCT_CHANGE_T1-T4&image2dataValue=-44.83&image2dataName=Clinical%20Response_T1-T4&image2dataValue=3=Stable%20Disease");

		selenium.type("MAINbody:sideBarView:loginForm:uName2", "nciadevtest");
		selenium.type("MAINbody:sideBarView:loginForm:pass2", "saicT3@m17");
		selenium.click("MAINbody:sideBarView:loginForm:loginButton2");
		selenium.waitForCondition("selenium.browserbot.getCurrentWindow().document.getElementById('MAINbody:imageForm:imageTable')",
                                  "30000");

		final String patientAttributesTable1 =
			//"id('MAINbody:imageForm:imageTable:patientAttributesTable')";
			"xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[1]/table[2]";

		final String patientAttributesTable2 =
			//"id('MAINbody:imageForm:imageTable:patientAttributesTable')";
			"xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[2]/table[2]";


        System.out.println("foo:"+selenium.getText("xpath=//table[@id='MAINbody:imageForm:imageTable']/tbody/tr/td[1]/table[1]/tbody/tr[1]/td"));


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
