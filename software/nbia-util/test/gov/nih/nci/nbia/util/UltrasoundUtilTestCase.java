/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.Ultrasound_Util;
import junit.framework.TestCase;

public class UltrasoundUtilTestCase extends TestCase {
	
	public void testGetCodeByGivenImageTypeLabel() {	
		assertEquals(Ultrasound_Util.getCodeByGivenImageTypeLabel("2D Imaging"), "0001");						
	}
	public void testGetTextByGivenImageTypeCode() {		
		assertEquals(Ultrasound_Util.getTextByGivenImageTypeCode("0010"), "Color Doppler");				
	}
	
}
