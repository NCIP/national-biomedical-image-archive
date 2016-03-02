/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.referencedimages;

import gov.nih.nci.nbia.AbstractSelenTestCaseImpl;

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

        navigateToISPYPortal(image1Label,
                             "2",
                             "1.2.124.113532.192.9.54.60.20020702.141304.3659576",
                             "1.2.840.113619.2.5.1762805546.3105.1025559471.58",
                             "1.2.840.113619.2.5.1762805546.3105.1025559471.276",
                             "Post",
                             "2",
                             "1.2.124.113532.192.9.54.60.20021230.122345.403213",
                             "1.2.840.113619.2.5.1762805546.2376.1041867495.89",
                             "1.2.840.113619.2.5.1762805546.2376.1041867495.189");

		assertEquals(getISPYImage1Label(), image1Label);

		assertEquals(getISPYDataName(1),
                     "Patient Id");
		assertEquals(getISPYDataName(2),
                     "Baseline Morphology");
		assertEquals(getISPYDataName(3),
                     "Longest Diameter_PCT_CHANGE_T1-T2");
		assertEquals(getISPYDataName(4),
                     "Clinical Response_T1-T2");

		assertEquals(getISPYDataValue(1),
                     "2");
        assertEquals(getISPYDataValue(2),
                     "3_Area enhancement with irregular margins - with nodularity");
        assertEquals(getISPYDataValue(3),
                     "-10.34");
        assertEquals(getISPYDataValue(4),
                     "3_Stable Disease");
	}
}
