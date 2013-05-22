/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import org.apache.log4j.Logger;

public class UidDisplayUtil {	
	private static transient Logger logger = Logger.getLogger(UidDisplayUtil.class);
    private static final String UID_DELIMETER = "...";    
    
    public static String getDisplayUid(String original) {
       String lengthString =System.getProperty("gov.nih.nci.ncia.ui.uid.display.length");
       int length = 100;
       if(lengthString != null && lengthString.length() > 0){
    	   try{
    		   length = Integer.valueOf(lengthString);
    	   }catch(NumberFormatException nfe){
    		   nfe.printStackTrace();
    		   logger.error("Display Length Property Not Set: gov.nih.nci.ncia.ui.uid.display.length");
    		   logger.error("Using default length of :"+length);
    	   }
       }
       //sit on this deployment error?  might want to fail fast instead to let folks know things are screwed up in properties-service.xml?
       if (original != null && original.length() > length) {

            StringBuilder uid = new StringBuilder();
            uid.append(original.subSequence(0, length / 2));
            uid.append(UID_DELIMETER);
            uid
                    .append(original.substring(original.length()
                            - (length / 2)));
            return uid.toString();
        } else {
            return original;
        }
    }
}
