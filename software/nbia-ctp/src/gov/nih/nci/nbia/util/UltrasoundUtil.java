/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Util class for Ultrasound process
 *
 * @author zhoujim
 *
 */
public class UltrasoundUtil {

    public static final int[] MASKS = {0x0001, 0x0002, 0x0004, 0x0008, 0x0010, 0x0020,0x0040, 0x0100, 0x0200};

	public static String getMultiModalityByCode(String code) throws Exception
	{
		NumberFormat nf = new DecimalFormat("#0000");
	    int intCode = Integer.valueOf(code, 16);
	    StringBuffer buff = new StringBuffer();
	    for(int i=0; i< MASKS.length;i++) {
	         if((MASKS[i] & intCode) > 0) {
	        	 String s = Integer.toString(MASKS[i],16);
	        	 long l = Long.parseLong(s);
	        	 buff.append(nf.format(l));
	        	 buff.append(',');
	         }
	   }
	   String result = buff.toString();
	   return result.substring(0,result.length()-1);
	}
}
