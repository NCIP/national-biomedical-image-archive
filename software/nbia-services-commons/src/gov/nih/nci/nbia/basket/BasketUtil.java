/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.basket;

import java.text.DecimalFormat;

public class BasketUtil {
    /**
     * Returns a string representation of the time it will take to download via
     * DSL.
     */
    public static String getDslDownload(double size) {
        double time = calculateTime((size / 1000000.0), 5493.1641);
        int hours = 0;
        int minutes = 0;

        if (time > 60) {
            hours = (int) time / 60;
            minutes = (int) time % 60;

            return hours + HOURS + minutes + MINUTES;
        } else {
            minutes = (int) time;

            return minutes + MINUTES;
        }
    }
    
    
    public static String getT1Download(double size) {
        double time = calculateTime((size / 1000000.0), 56762.6952);
        int hours = 0;
        int minutes = 0;

        if (time > 60) {
            hours = (int) time / 60;
            minutes = (int) time % 60;

            return hours + HOURS + minutes + MINUTES;
        } else {
            minutes = (int) time;

            return minutes + MINUTES;
        }
    }
    
    /**
     * Generates the file name based on the user's security information
     */
    public static String generateFileName(String emailOrUserName) {
        // Get a random number of up to 7 digits
        int sevenDigitRandom = new Double(Math.random() * 10000000).intValue();

        // Get the last 7 digits of the Java timestamp
        String s = String.valueOf(System.currentTimeMillis());
        String lastSevenOfTimeStamp = s.substring(s.length() - 7, s.length());

        // Put it all together
        return emailOrUserName + "_" + sevenDigitRandom +
            lastSevenOfTimeStamp + ".zip";
    }
    
    public static String getSizeString(double size) {
        String measurement = "";
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(3);

        double tempSize = size/1000000.0 ;

        measurement = BasketUtil.measureImage(tempSize);

        if (tempSize > 1000) {
            tempSize = tempSize / 1000.0;
        }

        return nf.format(tempSize) + measurement;
    }
    
    private static String measureImage(double size) {
        String measurement = "";

        if (size > 1000) {
            measurement = " GB";

        } else {
            measurement = " MB";
        }

        return measurement;
   }      
    
    /////////////////////////////////////////PRIVATE///////////////////////////////////////

    private static final String MINUTES = " minute(s)";
    private static final String HOURS = " hour(s), ";    

    /**
     * Calculates the total time it should take to dowload based upon connection
     * speed.
     *
     * @param fileSize size in bytes
     * @param speed
     */
    private static double calculateTime(double fileSize, double speed) {
        return ((fileSize * 1024) / speed);
    }             
}
