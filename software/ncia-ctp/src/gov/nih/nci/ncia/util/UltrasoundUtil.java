package gov.nih.nci.ncia.util;

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

	public static void main(String[] args) throws Exception{
		String result = null;
		try{
		 	result = getMultiModalityByCode("00tt4");
		}catch(Exception e){
			System.out.println("No big deal !!");
		}
		System.out.println("result: " + result );
	}
}
