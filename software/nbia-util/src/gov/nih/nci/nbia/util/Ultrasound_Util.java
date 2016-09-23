/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.util.HashMap;

public class Ultrasound_Util {

	public static final String[] MULTI_M_CODE = {"0001","0002","0004", "0008",
								 "0010", "0020","0040","0100", "0200"};

	public static final String[] MULTI_M_TEXT = {"2D Imaging", "M-Mode", "CW Doppler",
			"PW Doppler", "Color Doppler", "Color M-Mode", "3D Rendering", "Color Power Mode",
			"Tissue Characterization"};
	public static HashMap<String, String> mapping = new HashMap<String, String>();
	static {
		for(int index = 0; index < MULTI_M_CODE.length; index++)
		{
			mapping.put(MULTI_M_CODE[index], MULTI_M_TEXT[index]);
		}
	}

	public static HashMap<String, String> reverseMapping = new HashMap<String, String>();
	static {
		for(int index = 0; index < MULTI_M_CODE.length; index++)
		{
			reverseMapping.put(MULTI_M_TEXT[index], MULTI_M_CODE[index]);
		}
	}

	public static String getTextByGivenImageTypeCode(String code){
		return (String)mapping.get(code);
	}

	public static String getCodeByGivenImageTypeLabel(String label){
		return (String)reverseMapping.get(label);
	}
}
