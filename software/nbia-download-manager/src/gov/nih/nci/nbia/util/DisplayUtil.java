/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.text.DecimalFormat;

public class DisplayUtil {
	
    /*1 Byte = 8 Bit
    1 Kilobyte = 1024 Bytes
    1 Megabyte = 1048576 Bytes
    1 Gigabyte = 1073741824 Bytes*/

    public static String computeSizeString(long sizeInBytes) {
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        if(sizeInBytes < 1000){
            return nf.format(sizeInBytes) + " Bytes";
        }
        
        double totalSizeInKB = Long.valueOf (sizeInBytes).doubleValue()/1024.0;
        if(totalSizeInKB < 1000){
            return nf.format(totalSizeInKB) + " KB";
        }
        double totalSizeInMB = Long.valueOf (sizeInBytes).doubleValue()/1048576;
        if (totalSizeInMB < 1000) {
            return nf.format(totalSizeInMB) + " MB";
        }
        double totalSizeInGB = Long.valueOf (sizeInBytes).doubleValue()/1073741824; 
        return nf.format(totalSizeInGB) + " GB";		
    }
}
